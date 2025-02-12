package com.app.question.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.beans.factory.annotation.Autowired
import java.io.ByteArrayInputStream

@Component
class RequestLoggingFilter : Filter {

    private val logger: Logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    @Autowired
    lateinit var mapper: ObjectMapper

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest) {
            val logData = mutableMapOf<String, Any>()
            val wrappedRequest = CachedBodyHttpServletRequest(request)
            val headers: List<Pair<String, String>> = wrappedRequest.headerNames.asIterator()
                .asSequence()
                .map { it to wrappedRequest.getHeader(it) }
                .toList()

            logData["method"] = wrappedRequest.method
            logData["url"] = wrappedRequest.requestURI
            logData["headers"] = headers

            val body = wrappedRequest.getBody()
            if (body.isNotEmpty()) {
                logData["body"] = body
            }

            logger.info("🔹 Dados da Requisição: {}", mapper.writeValueAsString(logData))
            chain.doFilter(wrappedRequest, response)
        } else {
            chain.doFilter(request, response)
        }
    }
}

class CachedBodyHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val cachedBody: ByteArray = request.inputStream.readBytes()

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(cachedBody)
        return object : ServletInputStream() {
            override fun isFinished(): Boolean = byteArrayInputStream.available() == 0
            override fun isReady(): Boolean = true
            override fun setReadListener(listener: ReadListener) {}
            override fun read(): Int = byteArrayInputStream.read()
        }
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(getInputStream(), StandardCharsets.UTF_8))
    }

    fun getBody(): String {
        return String(cachedBody, StandardCharsets.UTF_8)
    }
}


