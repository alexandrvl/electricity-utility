package com.electricity.utility.domain.analytics.service

import com.electricity.utility.domain.analytics.model.*
import com.electricity.utility.domain.billing.model.ConsumptionReading
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class UsageAnalyticsImplTest {
    private lateinit var usageAnalytics: UsageAnalyticsImpl
    private lateinit var testPeriod: AnalysisPeriod
    private lateinit var testReadings: List<ConsumptionReading>

    @BeforeEach
    fun setup() {
        usageAnalytics = UsageAnalyticsImpl()
        
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)
        testPeriod = AnalysisPeriod(
            startDate = now.minusDays(7),
            endDate = now,
            granularity = TimeGranularity.HOURLY
        )
        
        // Generate test readings for a week
        testReadings = generateTestReadings(testPeriod)
    }

    @Test
    fun `test generate report returns valid report`() {
        val report = usageAnalytics.generateReport(testPeriod)
        
        assertNotNull(report)
        assertEquals(testPeriod, report.period)
        assertNotNull(report.patterns)
        assertNotNull(report.trends)
        assertNotNull(report.anomalies)
    }

    @Test
    fun `test analyze patterns identifies different pattern types`() {
        val patterns = usageAnalytics.analyzePatterns(testPeriod)
        
        assertTrue(patterns.isNotEmpty())
        assertTrue(patterns.any { it.type == PatternType.PEAK_USAGE })
        assertTrue(patterns.any { it.type == PatternType.BASELINE })
    }

    @Test
    fun `test identify trends detects consumption trends`() {
        val trends = usageAnalytics.identifyTrends(testPeriod)
        
        assertTrue(trends.isNotEmpty())
        assertTrue(trends.any { it.direction == TrendDirection.INCREASING || 
                              it.direction == TrendDirection.DECREASING || 
                              it.direction == TrendDirection.STABLE })
    }

    @Test
    fun `test detect anomalies identifies unusual consumption`() {
        val anomalies = usageAnalytics.detectAnomalies(testReadings)
        
        assertTrue(anomalies.isNotEmpty())
        assertTrue(anomalies.any { it.severity == AnomalySeverity.HIGH || 
                                 it.severity == AnomalySeverity.MEDIUM })
    }

    @Test
    fun `test anomaly callback is triggered for critical anomalies`() {
        var callbackTriggered = false
        usageAnalytics.registerAnomalyCallback { anomaly ->
            if (anomaly.severity == AnomalySeverity.CRITICAL) {
                callbackTriggered = true
            }
        }

        // Generate readings with a critical anomaly
        val anomalousReadings = generateAnomalousReadings()
        usageAnalytics.detectAnomalies(anomalousReadings)

        assertTrue(callbackTriggered)
    }

    private fun generateTestReadings(period: AnalysisPeriod): List<ConsumptionReading> {
        val readings = mutableListOf<ConsumptionReading>()
        var current = period.startDate

        while (current <= period.endDate) {
            val isPeakHour = current.hour in 9..17
            val baseConsumption = if (isPeakHour) 50.0 else 20.0
            val randomVariation = (-5..5).random()
            
            readings.add(ConsumptionReading(
                timestamp = current,
                kilowattHours = baseConsumption + randomVariation,
                isPeakHours = isPeakHour
            ))
            
            current = current.plusHours(1)
        }

        return readings
    }

    private fun generateAnomalousReadings(): List<ConsumptionReading> {
        val readings = generateTestReadings(testPeriod).toMutableList()
        
        // Add some critical anomalies
        val anomalyTime = testPeriod.startDate.plusDays(2)
        readings.add(ConsumptionReading(
            timestamp = anomalyTime,
            kilowattHours = 200.0, // Very high consumption
            isPeakHours = true
        ))

        return readings
    }
}