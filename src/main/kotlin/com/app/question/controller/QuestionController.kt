package com.app.question.controller

import com.app.question.domain.Question
import com.app.question.service.QuestionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/questions")
class QuestionController {

    @Autowired
    lateinit var questionService: QuestionService

    @GetMapping("/test")
    fun execute(): ResponseEntity<Any> {
        return ResponseEntity.ok("ANY TEST")
    }

    @PostMapping
    fun save(@RequestBody question: Question): ResponseEntity<Any> {
        val result = questionService.save(question)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun findAll(): ResponseEntity<Any> {
        val result = questionService.findAll()
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Any> {
        val result = questionService.delete(id)
        return if(result) ResponseEntity.noContent().build()
                else ResponseEntity.badRequest().build()
    }
}