package com.app.question

import com.app.question.config.S3Config
import com.app.question.domain.Question
import com.app.question.repository.QuestionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.util.UUID

@Transactional
@AutoConfigureMockMvc
@Import(AwsMockConfig::class)
@ComponentScan(
    basePackages = ["com.app.question.config"],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = [S3Config::class])]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var repository: QuestionRepository

    @Autowired
    lateinit var s3Client: S3Client // Verifique se este mock está sendo injetado

    @Autowired
    lateinit var s3Presigner: S3Presigner

    @Test
    fun `Testar injecao dos mocks`() {
       Assertions.assertNotNull(s3Client, "S3Client não foi injetado corretamente")
       Assertions.assertNotNull(s3Presigner, "S3Presigner não foi injetado corretamente")
    }

    @Test
    fun teste() {
        mockMvc.perform( MockMvcRequestBuilders.get("/v1/questions/test")
            .header("key", "value")
        ).andExpectAll(
            MockMvcResultMatchers.status().isOk,
            MockMvcResultMatchers.jsonPath("$").value("ANY TEST")
        )
    }

    @Test
    @WithMockUser(username = "user", roles = ["USER"])
    fun `Deve registrar uma pergunta`() {
        val request = Question(title = "teste", description = "uma descrição para teste")

        mockMvc.perform( MockMvcRequestBuilders.post("/v1/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .header("key", "value")
            .content(mapper.writeValueAsString(request))
        ).andExpectAll(
            MockMvcResultMatchers.status().isOk,
            MockMvcResultMatchers.jsonPath("$.id").exists(),
            MockMvcResultMatchers.jsonPath("$.title").value(request.title),
            MockMvcResultMatchers.jsonPath("$.description").value(request.description),
            MockMvcResultMatchers.jsonPath("$.createdAt").exists()
        )
    }

    @Test
    fun `Deve buscar todas as perguntas registradas`() {
        val recurrence = 50

        repeat(recurrence) {
            val request = Question(title = "teste-$it", description = "descrição para o teste-$it")
            repository.save(request.toEntity())
        }

        mockMvc.perform( MockMvcRequestBuilders.get("/v1/questions")
            .header("key", "value")
        ).andExpectAll(
            MockMvcResultMatchers.status().isOk,
            MockMvcResultMatchers.jsonPath("$[0].id").exists(),
            MockMvcResultMatchers.jsonPath("$.length()").value(recurrence)
        )
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `Deve remover uma pergunta`() {
        val request = Question(title = "teste", description = "descrição para o teste")

        val question = repository.save(request.toEntity())

        mockMvc.perform( MockMvcRequestBuilders.delete("/v1/questions/${question.id}")
            .header("key", "value")
        ).andExpectAll(
            MockMvcResultMatchers.status().isNoContent
        )
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `Nao deve remover uma pergunta`() {
        mockMvc.perform( MockMvcRequestBuilders.delete("/v1/questions/${UUID.randomUUID()}")
            .header("key", "value")
        ).andExpectAll(
            MockMvcResultMatchers.status().isBadRequest
        )
    }
}
