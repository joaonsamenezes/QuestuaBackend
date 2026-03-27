package com.questua.backend.storage

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Component
class LocalArchiveStorageProvider {

    private val baseDir = "uploads"

    fun store(file: MultipartFile, folder: String?): String {
        val safeFolder = folder
            ?.trim()
            ?.removePrefix("/")
            ?.removeSuffix("/")
            ?: "misc"

        val extension = file.originalFilename
            ?.substringAfterLast('.', "")
            ?.ifBlank { "bin" }
            ?: "bin"

        val targetDir = Paths.get(baseDir, safeFolder)
        Files.createDirectories(targetDir)

        val filename = "${UUID.randomUUID()}.$extension"
        val path = targetDir.resolve(filename)
        file.transferTo(path)

        return "/uploads/$safeFolder/$filename"
    }

    fun deleteByUrl(url: String) {
        if (url.startsWith("http://") || url.startsWith("https://")) return

        val relativePath = url.removePrefix("/uploads/")
        val path = Paths.get(baseDir, relativePath)
        Files.deleteIfExists(path) 
    }
}