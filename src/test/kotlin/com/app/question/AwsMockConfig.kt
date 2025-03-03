package com.app.question

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.mockito.Mockito
import org.springframework.context.annotation.Primary
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@TestConfiguration(proxyBeanMethods = true)
class AwsMockConfig {
    @Bean
    @Primary
    fun mockS3Client(): S3Client {
        return Mockito.mock(S3Client::class.java)
    }

    @Bean
    @Primary
    fun mockS3Presigner(): S3Presigner {
        return Mockito.mock(S3Presigner::class.java)
    }
}
