package com.questua.backend.mapper

import com.questua.backend.dto.DeviceInfoDTO
import com.questua.backend.dto.ReportRequestDTO
import com.questua.backend.dto.ReportResponseDTO
import com.questua.backend.model.DeviceInfo
import com.questua.backend.model.Report
import com.questua.backend.model.ReportStatus

object ReportMapper {

    fun toResponse(entity: Report): ReportResponseDTO =
        ReportResponseDTO(
            id = entity.id!!,
            userId = entity.userId,
            typeReport = entity.typeReport,
            descriptionReport = entity.descriptionReport,
            screenshotUrl = entity.screenshotUrl,
            deviceInfo = entity.deviceInfo?.let { toDTO(it) },
            appVersion = entity.appVersion,
            statusReport = entity.statusReport,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )

    fun toEntity(dto: ReportRequestDTO): Report =
        Report(
            userId = dto.userId,
            typeReport = dto.typeReport,
            descriptionReport = dto.descriptionReport,
            screenshotUrl = dto.screenshotUrl,
            deviceInfo = dto.deviceInfo?.let { toEntity(it) },
            appVersion = dto.appVersion,
            statusReport = dto.statusReport ?: ReportStatus.OPEN
        )

    fun toDTO(entity: DeviceInfo): DeviceInfoDTO =
        DeviceInfoDTO(
            deviceModel = entity.deviceModel,
            androidVersion = entity.androidVersion,
            screenWidth = entity.screenWidth,
            screenHeight = entity.screenHeight
        )

    fun toEntity(dto: DeviceInfoDTO): DeviceInfo =
        DeviceInfo(
            deviceModel = dto.deviceModel,
            androidVersion = dto.androidVersion,
            screenWidth = dto.screenWidth,
            screenHeight = dto.screenHeight
        )
}
