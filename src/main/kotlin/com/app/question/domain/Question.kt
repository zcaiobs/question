package com.app.question.domain

import java.time.LocalDateTime
import java.util.*

data class Question(
    val id: UUID? = null,
    val title: String? = null,
    val description: String? = null,
    val createdAt: LocalDateTime? = null,
) {
    fun toEntity(): QuestionEntity {
        return QuestionEntity(
            this.id, this.title, this.description, LocalDateTime.now()
        )
    }
}