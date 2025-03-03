package com.app.question

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QuestionApplication

fun main(args: Array<String>) {
	println("TESTE")
	runApplication<QuestionApplication>(*args)
}
