package com.questua.backend.model
import java.io.Serializable
 
import jakarta.persistence.*
import java.util.*

data class DeviceInfo(
    val deviceModel: String? = null,
    val androidVersion: String? = null,
    val screenWidth: Int? = null,
    val screenHeight: Int? = null
) : Serializable

@Entity
@Table(name = "report")
data class Report(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var userId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) 
    var typeReport: ReportType,

    @Column(nullable = false)
    var descriptionReport: String,

    var screenshotUrl: String? = null,

    @Column(columnDefinition = "jsonb")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    var deviceInfo: DeviceInfo? = null,

    var appVersion: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var statusReport: ReportStatus = ReportStatus.OPEN,

    @Column(nullable = false)
    var createdAt: Date = Date(),

    @Column(nullable = false)
    var updatedAt: Date = Date()
)

enum class ReportType {
    ERROR, FEEDBACK
}

enum class ReportStatus {
    OPEN, RESOLVED
}
