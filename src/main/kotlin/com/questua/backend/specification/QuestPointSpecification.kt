package com.questua.backend.specification

import com.questua.backend.filter.QuestPointFilter
import com.questua.backend.model.QuestPoint
import org.springframework.data.jpa.domain.Specification
import java.util.UUID
 
object QuestPointSpecification {

    fun fromFilter(filter: QuestPointFilter): Specification<QuestPoint> {
        var spec: Specification<QuestPoint> = Specification { _, _, cb -> cb.conjunction() }

        spec = spec.and(cityId(filter.cityId))
        spec = spec.and(title(filter.title))
        spec = spec.and(difficulty(filter.difficulty))
        spec = spec.and(premium(filter.isPremium))
        spec = spec.and(ai(filter.isAiGenerated))
        spec = spec.and(isPublished(filter.isPublished))

        return spec
    }

    private fun cityId(value: UUID?): Specification<QuestPoint>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("cityId"), it)
            }
        }

    private fun title(value: String?): Specification<QuestPoint>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("title")), "%${it.lowercase()}%")
            }
        }

    private fun difficulty(value: Short?): Specification<QuestPoint>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Short>("difficulty"), it)
            }
        }

    private fun premium(value: Boolean?): Specification<QuestPoint>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isPremium"), it)
            }
        }

    private fun ai(value: Boolean?): Specification<QuestPoint>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isAiGenerated"), it)
            }
        }

    private fun isPublished(value: Boolean?): Specification<QuestPoint>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isPublished"), it)
            }
        }
}