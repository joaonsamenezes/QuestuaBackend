package com.questua.backend.dto

import jakarta.validation.constraints.*
import java.util.*
import com.questua.backend.model.ReportType
import com.questua.backend.model.ReportStatus

data class DeviceInfoDTO(
    @field:Size(max = 100, message = "deviceModel deve ter no máximo 100 caracteres")
    val deviceModel: String? = null,

    @field:Size(max = 50, message = "androidVersion deve ter no máximo 50 caracteres")
    val androidVersion: String? = null,

    val screenWidth: Int? = null,
    val screenHeight: Int? = null
)

data class ReportRequestDTO(
    @field:NotNull(message = "userId é obrigatório")
    val userId: UUID,

    @field:NotNull(message = "typeReport é obrigatório")
    val typeReport: ReportType,

    @field:NotBlank(message = "descriptionReport não pode ser vazio")
    val descriptionReport: String,

    @field:Size(max = 255, message = "screenshotUrl deve ter no máximo 255 caracteres")
    val screenshotUrl: String? = null,

    val deviceInfo: DeviceInfoDTO? = null,

    @field:Size(max = 50, message = "appVersion deve ter no máximo 50 caracteres")
    val appVersion: String? = null,

    val statusReport: ReportStatus? = null
)

data class ReportResponseDTO(
    val id: UUID,
    val userId: UUID,
    val typeReport: ReportType,
    val descriptionReport: String,
    val screenshotUrl: String?,
    val deviceInfo: DeviceInfoDTO?,
    val appVersion: String?,
    val statusReport: ReportStatus,
    val createdAt: Date,
    val updatedAt: Date
)
 