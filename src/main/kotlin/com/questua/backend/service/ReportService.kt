package com.questua.backend.service

import com.questua.backend.event.FeedbackSubmittedEvent
import com.questua.backend.filter.ReportFilter
import com.questua.backend.model.Report
import com.questua.backend.repository.ReportRepository
import com.questua.backend.specification.ReportSpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Service
class ReportService(
    private val repository: ReportRepository,
    private val archiveStorageService: ArchiveStorageService,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun findAll(filter: ReportFilter, pageable: Pageable): Page<Report> =
        repository.findAll(ReportSpecification.fromFilter(filter), pageable)

    fun findById(id: UUID): Report =
        repository.findById(id).orElseThrow { EntityNotFoundException("Report not found: $id") }

    fun create(report: Report): Report {
        val saved = repository.save(report)
        
        eventPublisher.publishEvent(FeedbackSubmittedEvent(saved.userId))
        
        return saved
    }

    fun update(id: UUID, updated: Report): Report {
        val existing = findById(id)

        val oldScreenshot = existing.screenshotUrl
        val newScreenshot = updated.screenshotUrl
        if (oldScreenshot != null && oldScreenshot != newScreenshot) {
            archiveStorageService.delete(oldScreenshot)
        }

        val merged = existing.copy(
            userId = updated.userId,
            typeReport = updated.typeReport,
            descriptionReport = updated.descriptionReport,
            screenshotUrl = newScreenshot,
            deviceInfo = updated.deviceInfo,
            appVersion = updated.appVersion,
            statusReport = updated.statusReport,
            updatedAt = Date()
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        val existing = findById(id)
        existing.screenshotUrl?.let { archiveStorageService.delete(it) }
        repository.deleteById(id)
    }  
}