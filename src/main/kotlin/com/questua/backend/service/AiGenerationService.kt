package com.questua.backend.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.questua.backend.dto.*
import com.questua.backend.integration.GeminiClient
import com.questua.backend.model.*
import com.questua.backend.model.enums.*
import com.questua.backend.repository.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AiGenerationService(
    private val geminiClient: GeminiClient,
    private val questRepository: QuestRepository,
    private val questPointRepository: QuestPointRepository,
    private val sceneDialogueRepository: SceneDialogueRepository,
    private val characterRepository: CharacterEntityRepository,
    private val achievementRepository: AchievementRepository,
    private val cityRepository: CityRepository,
    private val aiLogRepository: AiGenerationLogRepository,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(AiGenerationService::class.java)
    private val currentModelName = "gemini-2.0-flash"

    @Transactional
    fun generateQuestPoint(dto: GenerateQuestPointRequestDTO, userId: UUID): QuestPoint {
        val city = cityRepository.findById(dto.cityId).orElseThrow { IllegalArgumentException("Cidade não encontrada") }
        
        val prompt = """
            Atue como um especialista em turismo.
            Crie um 'QuestPoint' na cidade: ${city.cityName} (${city.countryCode}).
            Tema: ${dto.theme}. Idioma: ${dto.targetLanguage}.
            
            REGRAS:
            1. Identifique um local real.
            2. Estime coordenadas (lat/lon).
            
            Retorne JSON (Objeto único):
            { 
                "title": "Nome do Local", 
                "descriptionQpoint": "Descrição curta", 
                "difficulty": 1, 
                "lat": -23.55, 
                "lon": -46.63, 
                "isPremium": false 
            }
        """.trimIndent()

        val node = callAiAndParse(prompt, userId, AiTargetType.QUEST_POINT, dto.cityId)

        return questPointRepository.save(QuestPoint(
            cityId = dto.cityId,
            title = node.path("title").asText("Ponto Gerado"),
            descriptionQpoint = node.path("descriptionQpoint").asText("Sem descrição"),
            difficulty = node.path("difficulty").asInt(1).toShort(),
            lat = node.path("lat").asDouble(0.0),
            lon = node.path("lon").asDouble(0.0),
            isPremium = node.path("isPremium").asBoolean(false),
            isAiGenerated = true
        ))
    }

    @Transactional
    fun generateQuest(dto: GenerateQuestRequestDTO, userId: UUID): Quest {
        val prompt = """
            Crie uma Quest. Contexto: ${dto.context}. Idioma: ${dto.targetLanguage}. Dificuldade: ${dto.difficultyLevel}.
            Retorne JSON (Objeto único):
            { 
                "title": "Título", 
                "descriptionQuest": "Descrição", 
                "xpValue": 100, 
                "learningFocus": { 
                    "grammarTopics": ["Tópico"], 
                    "vocabularyThemes": ["Tema"], 
                    "skills": ["listening"] 
                } 
            }
        """.trimIndent()

        val node = callAiAndParse(prompt, userId, AiTargetType.QUEST, dto.questPointId)

        return questRepository.save(Quest(
            questPointId = dto.questPointId,
            title = node.path("title").asText("Nova Missão"),
            descriptionQuest = node.path("descriptionQuest").asText("Sem descrição"),
            difficulty = dto.difficultyLevel.toShort(),
            xpValue = node.path("xpValue").asInt(100),
            learningFocus = if (node.has("learningFocus")) objectMapper.treeToValue(node.path("learningFocus"), LearningFocus::class.java) else null,
            isAiGenerated = true
        ))
    }

    @Transactional
    fun generateCharacter(dto: GenerateCharacterRequestDTO, userId: UUID): CharacterEntity {
        val prompt = """
            Crie um NPC. Arquétipo: ${dto.archetype}. Idioma: ${dto.targetLanguage}.
            Retorne JSON (Objeto único):
            { 
                "nameCharacter": "Nome Completo", 
                "persona": { 
                    "description": "Biografia", 
                    "traits": ["Traço"], 
                    "speakingStyle": "Estilo", 
                    "voiceTone": "Tom", 
                    "background": "Fundo" 
                } 
            }
        """.trimIndent()

        val node = callAiAndParse(prompt, userId, AiTargetType.CHARACTER, null)

        val nameChar = node.path("nameCharacter").asText("NPC Misterioso")

        return characterRepository.save(CharacterEntity(
            nameCharacter = nameChar,
            persona = if (node.has("persona")) objectMapper.treeToValue(node.path("persona"), Persona::class.java) else null,
            avatarUrl = "https://ui-avatars.com/api/?name=${nameChar.replace(" ", "+")}&background=random&size=256",
            isAiGenerated = true
        ))
    }

    @Transactional
    fun generateDialogue(dto: GenerateDialogueRequestDTO, userId: UUID): SceneDialogue {
        val speaker = characterRepository.findById(dto.speakerCharacterId).orElseThrow()
        val mode = try { InputMode.valueOf(dto.inputMode) } catch (e: Exception) { InputMode.NONE }

        val prompt = """
            Gere diálogo. Falante: ${speaker.nameCharacter}. Contexto: ${dto.context}. Idioma: ${dto.targetLanguage}. Modo: $mode.
            Retorne JSON (Objeto único):
            { 
                "descriptionDialogue": "Cenário", 
                "textContent": "Fala", 
                "expectedResponse": "Resposta (opcional)", 
                "choices": [ { "text": "Opção", "nextDialogueId": null } ] 
            }
        """.trimIndent()

        val node = callAiAndParse(prompt, userId, AiTargetType.SCENE_DIALOGUE, dto.questId)
        
        val choicesList = if (node.has("choices") && node.path("choices").isArray) {
            node.path("choices").map { Choice(it.path("text").asText("Opção"), null) }
        } else null

        return sceneDialogueRepository.save(SceneDialogue(
            descriptionDialogue = node.path("descriptionDialogue").asText("Cena"),
            backgroundUrl = "https://via.placeholder.com/800x600?text=Cena+AI",
            speakerCharacterId = dto.speakerCharacterId,
            textContent = node.path("textContent").asText("..."),
            inputMode = mode,
            expectsUserResponse = mode != InputMode.NONE,
            expectedResponse = if (node.has("expectedResponse")) node.path("expectedResponse").asText() else null,
            choices = choicesList,
            isAiGenerated = true
        ))
    }

    @Transactional
    fun generateAchievement(dto: GenerateAchievementRequestDTO, userId: UUID): Achievement {
        // CORREÇÃO: Prompt explícito sobre os valores do Enum permitidos
        val prompt = """
            VOCÊ É UM GAMIFICATION EXPERT.
            OBJETIVO: Criar uma Conquista (Badge/Achievement).
            AÇÃO GATILHO: ${dto.triggerAction}.
            DIFICULDADE: ${dto.difficulty}.
            
            IMPORTANTE SOBRE RARIDADE: Use APENAS um destes valores: COMMON, RARE, EPIC, LEGENDARY.
            
            RETORNE APENAS O JSON (SEM MARKDOWN) NESTA ESTRUTURA:
            { 
                "keyName": "slug_em_ingles_snake_case", 
                "nameAchievement": "Nome Criativo", 
                "descriptionAchievement": "Descrição", 
                "rarity": "COMMON", 
                "xpReward": 100, 
                "metadata": { "category": "Geral", "descriptionExtra": "Lore" } 
            }
        """.trimIndent()

        val node = callAiAndParse(prompt, userId, AiTargetType.ACHIEVEMENT, null)
        
        val rarityStr = node.path("rarity").asText("COMMON").uppercase()
        val rarityEnum = try { RarityType.valueOf(rarityStr) } catch (e: Exception) { 
            // Fallback inteligente: se a IA mandar "HARD", mapeia para RARE ou EPIC
            if (rarityStr.contains("HARD")) RarityType.RARE else RarityType.COMMON 
        }

        return achievementRepository.save(Achievement(
            keyName = node.path("keyName").asText("ach_gen") + "_" + UUID.randomUUID().toString().substring(0, 4),
            nameAchievement = node.path("nameAchievement").asText("Nova Conquista"),
            descriptionAchievement = node.path("descriptionAchievement").asText("Sem descrição"),
            rarity = rarityEnum,
            xpReward = node.path("xpReward").asInt(50),
            metadata = if (node.has("metadata")) objectMapper.treeToValue(node.path("metadata"), AchievementMetadata::class.java) else null,
            iconUrl = "https://via.placeholder.com/150?text=Trofeu"
        ))
    }

    private fun callAiAndParse(prompt: String, userId: UUID, type: AiTargetType, targetId: UUID?): JsonNode {
        val startTime = System.currentTimeMillis()
        var responseTextRaw = ""
        
        try {
            logger.info(">>> PROMPT IA ($type): $prompt")
            
            val response = geminiClient.generate(prompt)
            val duration = System.currentTimeMillis() - startTime
            
            responseTextRaw = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: throw RuntimeException("Resposta IA vazia/nula")
            
            val cleanJson = responseTextRaw
                .replace("```json", "")
                .replace("```", "")
                .trim()
            
            logger.info("<<< JSON IA ($type): $cleanJson")

            var jsonNode = objectMapper.readTree(cleanJson)

            // CORREÇÃO CRÍTICA: Se a IA retornou uma lista [{}], pegamos o primeiro elemento
            if (jsonNode.isArray && !jsonNode.isEmpty) {
                logger.warn("IA retornou um ARRAY. Usando o primeiro elemento.")
                jsonNode = jsonNode.get(0)
            }

            val meta = AiGenerationResponseMeta(
                tokensUsed = response.usageMetadata?.totalTokenCount,
                durationSeconds = duration / 1000.0,
                latencyMs = duration,
                extraInfo = "Success"
            )
            aiLogRepository.save(AiGenerationLog(
                userId = userId, targetType = type, targetId = targetId, prompt = prompt,
                modelName = currentModelName, responseText = cleanJson, responseMeta = meta,
                statusGeneration = AiGenerationStatus.SUCCESS
            ))
            
            return jsonNode

        } catch (e: Exception) {
            logger.error("ERRO IA: ${e.message}")
            aiLogRepository.save(AiGenerationLog(
                userId = userId, targetType = type, targetId = targetId, prompt = prompt,
                modelName = currentModelName, responseText = "ERRO: ${e.message} | RAW: $responseTextRaw", 
                statusGeneration = AiGenerationStatus.ERROR
            ))
            throw e
        }
    }
} 