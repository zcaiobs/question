package com.app.question.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config {

    @Autowired
    lateinit var awsProperties: AwsProperties

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials()))
            .region(Region.of(awsProperties.region))
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        return S3Presigner.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials()))
            .region(Region.of(awsProperties.region))
            .build()
    }

    fun credentials(): AwsBasicCredentials = AwsBasicCredentials.create(awsProperties.accessKeyId, awsProperties.secretAccessKey)
}
