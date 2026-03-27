package com.questua.backend.service

import com.questua.backend.storage.LocalArchiveStorageProvider
import com.questua.backend.storage.CloudStorageProvider
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ArchiveStorageService(
    private val localProvider: LocalArchiveStorageProvider,
    private val cloudProvider: CloudStorageProvider
) {

    fun store(file: MultipartFile, folder: String?): String {
        return localProvider.store(file, folder)
    }

    fun delete(url: String) {
        localProvider.deleteByUrl(url)
    }
} 
