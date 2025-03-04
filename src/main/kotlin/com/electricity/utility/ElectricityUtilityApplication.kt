package com.electricity.utility

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ElectricityUtilityApplication

fun main(args: Array<String>) {
    runApplication<ElectricityUtilityApplication>(*args)
}