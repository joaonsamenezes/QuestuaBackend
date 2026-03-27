package com.questua.backend.repository

import com.questua.backend.model.CharacterEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CharacterEntityRepository :
    JpaRepository<CharacterEntity, UUID>,
    JpaSpecificationExecutor<CharacterEntity>
