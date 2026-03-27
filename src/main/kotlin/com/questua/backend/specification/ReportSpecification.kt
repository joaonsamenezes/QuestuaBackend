package com.questua.backend.specification

import com.questua.backend.filter.ReportFilter
import com.questua.backend.model.Report
import com.questua.backend.model.ReportStatus
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object ReportSpecification {

    fun fromFilter(filter: ReportFilter): Specification<Report> {
        var spec: Specification<Report> =
            Specification { root, _, cb -> cb.conjunction() }

        spec = spec.and(user(filter.userId))
        spec = spec.and(status(filter.statusReport))
        spec = spec.and(typeReport(filter.typeReport))

        return spec
    }

    private fun user(value: UUID?): Specification<Report>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("userId"), it)
            }
        }

    private fun status(value: ReportStatus?): Specification<Report>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<ReportStatus>("statusReport"), it)
            }
        }

    private fun typeReport(value: String?): Specification<Report>? =
        value?.let {
            Specification { root, _, cb ->
                cb.like(cb.lower(root.get("typeReport")), "%${it.lowercase()}%")
            }
        }
}
