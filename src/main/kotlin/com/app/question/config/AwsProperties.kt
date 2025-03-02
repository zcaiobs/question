package com.app.question.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "var")
data class AwsProperties(
    var accessKeyId: String? = null,
    var secretAccessKey: String? = null,
    var region: String? = null
)
