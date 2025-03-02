package com.app.question.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    val path = "/v1/**"

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}123456").roles("USER").build()
        val admin = User.withUsername("admin").password("{noop}123456").roles("ADMIN").build()
        return InMemoryUserDetailsManager(user, admin)
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers( "/v1/images").permitAll()
                    .requestMatchers( "/v1/images/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/actuator").permitAll()
                    .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .requestMatchers(HttpMethod.GET, path).permitAll()
                    .requestMatchers(HttpMethod.POST, path).hasRole("USER")
                    .requestMatchers(HttpMethod.PUT, path).hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, path).hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .httpBasic(withDefaults())
            .build()
    }
}