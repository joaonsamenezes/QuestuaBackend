package com.questua.backend.specification

import com.questua.backend.filter.AchievementFilter
import com.questua.backend.model.Achievement
import org.springframework.data.jpa.domain.Specification
import com.questua.backend.model.enums.RarityType

object AchievementSpecification {

    fun fromFilter(filter: AchievementFilter): Specification<Achievement> {
        var spec: Specification<Achievement> =
            Specification { root, _, cb -> cb.conjunction() }

        spec = spec.and(keyName(filter.keyName))
        spec = spec.and(rarity(filter.rarity)) 
        spec = spec.and(minXp(filter.minXp))
        spec = spec.and(maxXp(filter.maxXp))

        return spec
    }

    private fun keyName(value: String?): Specification<Achievement>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("keyName")), "%${it.lowercase()}%")
            }
        }

    private fun rarity(value: RarityType?): Specification<Achievement>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<RarityType>("rarity"), it)
            }
        }

    private fun minXp(value: Int?): Specification<Achievement>? =
        value?.let {
            Specification { root, _, cb ->
                cb.greaterThanOrEqualTo(root.get("xpReward"), it)
            }
        }

    private fun maxXp(value: Int?): Specification<Achievement>? =
        value?.let {
            Specification { root, _, cb ->
                cb.lessThanOrEqualTo(root.get("xpReward"), it)
            }
        }
}
 