package com.questua.backend.specification

import com.questua.backend.filter.AiGenerationLogFilter
import com.questua.backend.model.AiGenerationLog
import com.questua.backend.model.AiGenerationStatus
import com.questua.backend.model.AiTargetType
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object AiGenerationLogSpecification {

    fun fromFilter(filter: AiGenerationLogFilter): Specification<AiGenerationLog> {
        var spec: Specification<AiGenerationLog> =
            Specification { root, _, cb -> cb.conjunction() } 

        spec = spec.and(user(filter.userId))
        spec = spec.and(targetType(filter.targetType))
        spec = spec.and(status(filter.statusGeneration))
        spec = spec.and(modelName(filter.modelName))
        spec = spec.and(targetId(filter.targetId))

        return spec
    }

    private fun user(value: UUID?): Specification<AiGenerationLog>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("userId"), it)
            }
        }

    private fun targetType(value: AiTargetType?): Specification<AiGenerationLog>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<AiTargetType>("targetType"), it)
            }
        }

    private fun status(value: AiGenerationStatus?): Specification<AiGenerationLog>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<AiGenerationStatus>("statusGeneration"), it)
            }
        }

    private fun modelName(value: String?): Specification<AiGenerationLog>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("modelName")), "%${it.lowercase()}%")
            }
        }

    private fun targetId(value: UUID?): Specification<AiGenerationLog>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("targetId"), it)
            }
        }
}
