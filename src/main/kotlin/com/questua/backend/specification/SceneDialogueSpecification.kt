package com.questua.backend.specification

import com.questua.backend.filter.SceneDialogueFilter
import com.questua.backend.model.SceneDialogue
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object SceneDialogueSpecification {

    fun fromFilter(filter: SceneDialogueFilter): Specification<SceneDialogue> {
        var spec: Specification<SceneDialogue> =
            Specification { root, _, cb -> cb.conjunction() }

        spec = spec.and(speaker(filter.speakerCharacterId))
        spec = spec.and(text(filter.text))

        return spec
    }

    private fun speaker(id: UUID?): Specification<SceneDialogue>? =
        id?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("speakerCharacterId"), it)
            }
        }

    private fun text(value: String?): Specification<SceneDialogue>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("textContent")), "%${it.lowercase()}%")
            }
        }
}
