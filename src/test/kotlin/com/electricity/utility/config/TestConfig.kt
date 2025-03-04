package com.electricity.utility.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@TestConfiguration
@EnableWebMvc
class TestConfig : WebMvcConfigurer {
    init {
        println("[DEBUG_LOG] Initializing TestConfig")
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        println("[DEBUG_LOG] Creating ObjectMapper bean")
        return ObjectMapper().registerModule(KotlinModule.Builder().build())
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        println("[DEBUG_LOG] Configuring message converters")
        converters.add(jsonMessageConverter(objectMapper()))
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        println("[DEBUG_LOG] Configuring content negotiation")
        configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .favorParameter(false)
    }

    @Bean
    fun jsonMessageConverter(objectMapper: ObjectMapper): MappingJackson2HttpMessageConverter {
        println("[DEBUG_LOG] Creating JSON message converter")
        val converter = MappingJackson2HttpMessageConverter(objectMapper)
        converter.supportedMediaTypes = listOf(MediaType.APPLICATION_JSON)
        return converter
    }
}
