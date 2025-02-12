package com.app.question.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "question")
class QuestionEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,
    @Column(name = "title")
    val title: String?,
    @Column(name = "description")
    val description: String?,
    @Column(name = "createdAt")
    val createdAt: LocalDateTime?
) {
    fun toDomain(): Question {
        return Question(
            this.id, this.title, this.description, this.createdAt
        )
    }
}