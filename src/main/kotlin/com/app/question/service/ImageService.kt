package com.app.question.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URL
import java.time.Duration

@Service
class ImageService {

    @Autowired
    lateinit var presigner: S3Presigner

    @Autowired
    lateinit var s3Client: S3Client

    val bucketName = "images-generate-test"

    fun readFile(bucketName: String, fileName: String): URL = presigner
        .presignGetObject(
            GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(GetObjectRequest.builder().bucket(bucketName).key(fileName).build())
                .build()
        ).url()

    fun listFiles(): List<String> = s3Client
        .listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).build())
        .contents()
        .map(S3Object::key)

    fun uploadFile(fileName: String, contentType: String): URL {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .contentType(contentType)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(30))
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedRequest = presigner.presignPutObject(presignRequest)

        return presignedRequest.url()
    }

    fun removeFile(fileName: String): Boolean = try {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(fileName).build())
        true
    } catch (ex: Exception) {
        false
    }
}