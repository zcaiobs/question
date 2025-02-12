package com.app.question.repository

import com.app.question.domain.QuestionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuestionRepository: JpaRepository<QuestionEntity, UUID> {}