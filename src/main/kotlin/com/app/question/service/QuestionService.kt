package com.app.question.service

import com.app.question.domain.Question
import com.app.question.repository.QuestionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class QuestionService {

    @Autowired
    lateinit var questionRepository: QuestionRepository

    fun save(question: Question): Question {
        return questionRepository.save(question.toEntity()).toDomain()
    }

    fun findAll(): List<Question> {
        return questionRepository.findAll().map { it.toDomain() }
    }

    fun delete(id: UUID): Boolean {
        val result =  questionRepository.findById(id)
        return if(result.isPresent) {
            questionRepository.deleteById(id)
            true
        } else false
    }
}