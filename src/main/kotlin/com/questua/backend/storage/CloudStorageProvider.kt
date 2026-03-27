package com.questua.backend.storage

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class CloudStorageProvider {

    fun store(file: MultipartFile): String {
        // Esqueleto para implementação de persistencia remota futura (se implementada)
        throw NotImplementedError("Cloud storage not implemented yet")
    }
}
