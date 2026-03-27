package com.questua.backend.specification

import com.questua.backend.filter.CharacterEntityFilter
import com.questua.backend.model.CharacterEntity
import org.springframework.data.jpa.domain.Specification

object CharacterEntitySpecification {

    fun fromFilter(filter: CharacterEntityFilter): Specification<CharacterEntity> {
        var spec: Specification<CharacterEntity> =
            Specification { root, _, cb -> cb.conjunction() }

        spec = spec.and(name(filter.nameCharacter))
        spec = spec.and(persona(filter.persona))
        spec = spec.and(isAi(filter.isAiGenerated))

        return spec
    }

    private fun name(value: String?): Specification<CharacterEntity>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("nameCharacter")), "%${it.lowercase()}%")
            }
        }

    private fun persona(value: String?): Specification<CharacterEntity>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("persona")), "%${it.lowercase()}%")
            }
        }

    private fun isAi(value: Boolean?): Specification<CharacterEntity>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("isAiGenerated"), it)
            }
        }
}
