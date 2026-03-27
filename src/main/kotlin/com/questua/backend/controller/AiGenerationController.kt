package com.questua.backend.controller

import com.questua.backend.dto.*
import com.questua.backend.mapper.*
import com.questua.backend.service.AiGenerationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/ai")
class AiGenerationController(
    private val aiService: AiGenerationService
) {
    private fun getAdminId(): UUID = 
        UUID.fromString(SecurityContextHolder.getContext().authentication.name)

    @PostMapping("/quest-point")
    fun generateQuestPoint(@Valid @RequestBody dto: GenerateQuestPointRequestDTO): ResponseEntity<QuestPointResponseDTO> {
        val result = aiService.generateQuestPoint(dto, getAdminId())
        return ResponseEntity.status(HttpStatus.CREATED).body(QuestPointMapper.toResponse(result))
    }

    @PostMapping("/quest")
    fun generateQuest(@Valid @RequestBody dto: GenerateQuestRequestDTO): ResponseEntity<QuestResponseDTO> {
        val result = aiService.generateQuest(dto, getAdminId())
        return ResponseEntity.status(HttpStatus.CREATED).body(QuestMapper.toResponse(result))
    }

    @PostMapping("/character")
    fun generateCharacter(@Valid @RequestBody dto: GenerateCharacterRequestDTO): ResponseEntity<CharacterEntityResponseDTO> {
        val result = aiService.generateCharacter(dto, getAdminId())
        return ResponseEntity.status(HttpStatus.CREATED).body(CharacterEntityMapper.toResponse(result))
    }

    @PostMapping("/dialogue")
    fun generateDialogue(@Valid @RequestBody dto: GenerateDialogueRequestDTO): ResponseEntity<SceneDialogueResponseDTO> {
        val result = aiService.generateDialogue(dto, getAdminId())
        return ResponseEntity.status(HttpStatus.CREATED).body(SceneDialogueMapper.toResponse(result))
    }

    @PostMapping("/achievement")
    fun generateAchievement(@Valid @RequestBody dto: GenerateAchievementRequestDTO): ResponseEntity<AchievementResponseDTO> {
        val result = aiService.generateAchievement(dto, getAdminId())
        return ResponseEntity.status(HttpStatus.CREATED).body(AchievementMapper.toResponse(result))
    }
}