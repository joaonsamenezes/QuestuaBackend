package com.questua.backend.controller

import com.questua.backend.service.ArchiveStorageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/upload")
class UploadController(private val archiveStorageService: ArchiveStorageService) {

    @PostMapping("/archive")
    fun uploadArchive(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("folder", required = false) folder: String?
    ): ResponseEntity<Map<String, String>> {
        val url = archiveStorageService.store(file, folder)
        return ResponseEntity.ok(mapOf("url" to url))
    }
 
    @DeleteMapping("/archive")
    fun deleteArchive(@RequestParam("url") url: String): ResponseEntity<Void> {
        archiveStorageService.delete(url)
        return ResponseEntity.noContent().build()
    }
} 
 