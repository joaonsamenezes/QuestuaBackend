package com.questua.backend.filter

import com.questua.backend.model.ReportStatus
import java.util.UUID

data class ReportFilter(
    val userId: UUID? = null,
    val statusReport: ReportStatus? = null,
    val typeReport: String? = null
)
