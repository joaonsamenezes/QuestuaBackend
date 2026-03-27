package com.questua.backend.model

import com.questua.backend.model.enums.AchievementConditionType
import com.questua.backend.model.enums.RarityType
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@Table(name = "achievement")
data class Achievement(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "key_name")
    var keyName: String? = null,

    @Column(name = "name_achievement", nullable = false)
    var nameAchievement: String = "",

    @Column(name = "description_achievement", nullable = false)
    var descriptionAchievement: String = "",

    @Column(name = "icon_url")
    var iconUrl: String? = null,

    @Column(name = "xp_reward")
    var xpReward: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rarity: RarityType = RarityType.COMMON,

    @Column(name = "is_hidden")
    var isHidden: Boolean = false,

    var category: String? = null,

    @Column(name = "is_global")
    var isGlobal: Boolean = true,

    @Column(name = "language_id")
    var languageId: UUID? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type")
    var conditionType: AchievementConditionType = AchievementConditionType.COMPLETE_SPECIFIC_QUEST,

    @Column(name = "target_id")
    var targetId: UUID? = null,

    @Column(name = "required_amount")
    var requiredAmount: Int = 1,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var metadata: AchievementMetadata? = null
)

data class AchievementMetadata(
    var data: String? = null
)