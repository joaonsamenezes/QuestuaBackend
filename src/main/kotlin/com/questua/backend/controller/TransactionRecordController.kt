package com.questua.backend.controller

import com.questua.backend.dto.TransactionRecordRequestDTO
import com.questua.backend.dto.TransactionRecordResponseDTO
import com.questua.backend.filter.TransactionRecordFilter
import com.questua.backend.mapper.TransactionRecordMapper
import com.questua.backend.service.TransactionRecordService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/transactions")
class TransactionRecordController(private val service: TransactionRecordService) {

    @GetMapping
    fun list(filter: TransactionRecordFilter, pageable: Pageable): Page<TransactionRecordResponseDTO> =
        service.findAll(filter, pageable).map { TransactionRecordMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): TransactionRecordResponseDTO =
        TransactionRecordMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: TransactionRecordRequestDTO): TransactionRecordResponseDTO =
        TransactionRecordMapper.toResponse(service.create(TransactionRecordMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody dto: TransactionRecordRequestDTO): TransactionRecordResponseDTO =
        TransactionRecordMapper.toResponse(service.update(id, TransactionRecordMapper.toEntity(dto)))

    @DeleteMapping("/{id}") 
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}
