package com.app.question.controller

import com.app.question.service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/images")
class ImageController {

    @Autowired
    lateinit var imageService: ImageService

    @GetMapping("/upload")
    fun uploadFile(@RequestParam fileName: String, @RequestParam contentType: String): ResponseEntity<Any> {
        val response = imageService.uploadFile(fileName, contentType)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/download")
    fun downloadFile(@RequestParam bucketName: String, @RequestParam fileName: String): ResponseEntity<Any> {
        val result = imageService.readFile(bucketName, fileName)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun listFiles(): ResponseEntity<Any> {
        val result = imageService.listFiles()
        return ResponseEntity.ok(result)
    }

    @DeleteMapping
    fun removeFile(@RequestParam fileName: String): ResponseEntity<Any> {
        val result = imageService.removeFile(fileName)
        return ResponseEntity.ok(result)
    }
}