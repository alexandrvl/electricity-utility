package com.electricity.utility.domain.analytics.service

import com.electricity.utility.domain.analytics.model.*
import com.electricity.utility.domain.billing.model.ConsumptionReading
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicReference

class UsageAnalyticsImplTest {
    private lateinit var analytics: UsageAnalyticsImpl

    @BeforeEach
    fun setup() {
        analytics = UsageAnalyticsImpl()
    }

    @Test
    fun `should generate usage report`() {
        // Given
        val period = AnalysisPeriod(
            startDate = LocalDateTime.now().minusDays(7),
            endDate = LocalDateTime.now(),
            granularity = TimeGranularity.DAILY
        )

        // When
        val report = analytics.generateReport(period)

        // Then
        assertNotNull(report)
        assertEquals(period, report.period)
        assertTrue(report.patterns.isNotEmpty())
        assertTrue(report.trends.isNotEmpty())
        assertNotNull(report.generatedAt)
    }

    @Test
    fun `should analyze consumption patterns`() {
        // Given
        val period = AnalysisPeriod(
            startDate = LocalDateTime.now().minusDays(7),
            endDate = LocalDateTime.now(),
            granularity = TimeGranularity.DAILY
        )

        // When
        val patterns = analytics.analyzePatterns(period)

        // Then
        assertTrue(patterns.isNotEmpty())
        assertTrue(patterns.any { it.type == PatternType.PEAK_USAGE })
        assertTrue(patterns.any { it.type == PatternType.OFF_PEAK_USAGE })
        patterns.forEach { pattern ->
            assertTrue(pattern.confidence in 0.0..1.0)
            assertTrue(pattern.readings.isNotEmpty())
        }
    }

    @Test
    fun `should identify consumption trends`() {
        // Given
        val period = AnalysisPeriod(
            startDate = LocalDateTime.now().minusDays(7),
            endDate = LocalDateTime.now(),
            granularity = TimeGranularity.DAILY
        )

        // When
        val trends = analytics.identifyTrends(period)

        // Then
        assertTrue(trends.isNotEmpty())
        assertTrue(trends.any { it.direction == TrendDirection.INCREASING })
        assertTrue(trends.any { it.direction == TrendDirection.STABLE })
        trends.forEach { trend ->
            assertTrue(trend.magnitude > 0)
            assertNotNull(trend.description)
        }
    }

    @Test
    fun `should detect anomalies in consumption readings`() {
        // Given
        val readings = listOf(
            ConsumptionReading(LocalDateTime.now(), 1.0, false),
            ConsumptionReading(LocalDateTime.now(), 1.2, false),
            ConsumptionReading(LocalDateTime.now(), 8.0, false), // Extreme anomaly
            ConsumptionReading(LocalDateTime.now(), 1.1, false)
        )

        // When
        val anomalies = analytics.detectAnomalies(readings)

        // Then
        assertTrue(anomalies.isNotEmpty())
        assertEquals(1, anomalies.size)
        assertEquals(AnomalySeverity.HIGH, anomalies[0].severity)
        assertEquals(8.0, anomalies[0].affectedReadings[0].kilowattHours)
    }

    @Test
    fun `should notify callbacks when anomaly is detected`() {
        // Given
        val readings = listOf(
            ConsumptionReading(LocalDateTime.now(), 1.0, false),
            ConsumptionReading(LocalDateTime.now(), 8.0, false) // Extreme anomaly
        )
        val detectedAnomaly = AtomicReference<ConsumptionAnomaly>()

        // When
        analytics.registerAnomalyCallback { anomaly ->
            detectedAnomaly.set(anomaly)
        }
        analytics.detectAnomalies(readings)

        // Then
        assertNotNull(detectedAnomaly.get())
        assertEquals(AnomalySeverity.HIGH, detectedAnomaly.get().severity)
    }

    @Test
    fun `should report monitoring status`() {
        // Then
        assertTrue(analytics.isMonitoringActive())
    }

    @Test
    fun `should cache analysis results for same period`() {
        // Given
        val period = AnalysisPeriod(
            startDate = LocalDateTime.now().minusDays(7),
            endDate = LocalDateTime.now(),
            granularity = TimeGranularity.DAILY
        )

        // When
        val patterns1 = analytics.analyzePatterns(period)
        val patterns2 = analytics.analyzePatterns(period)

        // Then
        assertEquals(patterns1, patterns2)
    }
}
