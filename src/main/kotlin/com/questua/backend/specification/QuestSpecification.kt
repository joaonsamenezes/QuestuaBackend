package com.questua.backend.specification

import com.questua.backend.filter.QuestFilter
import com.questua.backend.model.Quest
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object QuestSpecification {

    fun fromFilter(filter: QuestFilter): Specification<Quest> {
        var spec: Specification<Quest> =
            Specification { root, _, cb -> cb.conjunction() }

        spec = spec.and(title(filter.title))
        spec = spec.and(questPoint(filter.questPointId))
        spec = spec.and(difficulty(filter.difficulty))
        spec = spec.and(premium(filter.isPremium))
        spec = spec.and(ai(filter.isAiGenerated))
        spec = spec.and(isPublished(filter.isPublished))

        return spec
    }

    private fun title(value: String?): Specification<Quest>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("title")), "%${it.lowercase()}%")
            }
        }

    private fun questPoint(value: UUID?): Specification<Quest>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("questPointId"), it)
            }
        }

    private fun difficulty(value: Int?): Specification<Quest>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Int>("difficulty"), it)
            }
        }

    private fun premium(value: Boolean?): Specification<Quest>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isPremium"), it)
            }
        }

    private fun ai(value: Boolean?): Specification<Quest>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isAiGenerated"), it)
            }
        }

    private fun isPublished(value: Boolean?): Specification<Quest>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isPublished"), it)
            }
        }
} 