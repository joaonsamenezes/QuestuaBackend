package com.questua.backend.config

import com.questua.backend.exception.CustomAccessDeniedHandler
import com.questua.backend.exception.CustomAuthenticationEntryPoint
import com.questua.backend.security.JwtFilter
import com.questua.backend.security.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
class SecurityConfig(
    private val jwt: JwtUtil,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }

        http.cors { cors ->
            cors.configurationSource { _ ->
                CorsConfiguration().apply {
                    allowedOrigins = listOf("*")
                    allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    allowedHeaders = listOf("*")
                    allowCredentials = true
                }
            }
        }

        http.authorizeHttpRequests { auth ->
            auth.requestMatchers(
                "/api/auth/login", 
                "/api/auth/register", 
                "/api/auth/google-sync",
                "/api/auth/forgot-password",
                "/api/auth/reset-password",
                "/api/auth/register/init",
                "/api/auth/register/verify"
            ).permitAll()
            
            auth.requestMatchers(HttpMethod.POST, "/api/webhooks/stripe").permitAll()
            auth.requestMatchers("/uploads/**").permitAll()
            auth.requestMatchers(HttpMethod.GET, "/api/languages/**").permitAll()
             
            auth.requestMatchers(HttpMethod.GET, 
                "/api/languages/**",
                "/api/cities/**",
                "/api/quest-points/**",
                "/api/quests/**",
                "/api/characters/**",
                "/api/scene-dialogues/**",
                "/api/achievements/**",
                "/api/products/**",
                "/api/user-quests/**",
                "/api/user-achievements/**",
                "/api/transactions/**",
                "/api/users/**",
                "/api/user-languages/**",
                "/api/adventurer-tiers/**"
            ).authenticated()

            auth.requestMatchers(HttpMethod.POST, 
                "/api/payments/initiate", 
                "/api/user-quests/**",
                "/api/user-languages/**", 
                "/api/reports/**",
                "/api/upload/**"
            ).authenticated()

            auth.requestMatchers(HttpMethod.PUT, 
                "/api/users/**",
                "/api/user-languages/**"
            ).authenticated()

            auth.anyRequest().hasRole("ADMIN")
        }

        http.addFilterBefore(JwtFilter(jwt), UsernamePasswordAuthenticationFilter::class.java)

        http.exceptionHandling { exceptions ->
            exceptions
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint)
        }

        return http.build()
    }
}