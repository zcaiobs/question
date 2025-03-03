package com.app.question.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.*
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config {

    @Bean
    fun s3Client(awsCredentials: AwsCredentials): S3Client {
        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .region(Region.US_EAST_1)
            .build()
    }

    @Bean
    fun s3Presigner(awsCredentials: AwsCredentials): S3Presigner {
        return S3Presigner.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .region(Region.US_EAST_1)
            .build()
    }

    @Bean
    fun awsCredentialsProvider(): AwsCredentials {
        return ProfileCredentialsProvider.create().resolveCredentials()
    }
}
