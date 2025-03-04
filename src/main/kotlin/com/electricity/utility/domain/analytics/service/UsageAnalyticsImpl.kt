package com.electricity.utility.domain.analytics.service

import com.electricity.utility.domain.analytics.model.*
import com.electricity.utility.domain.billing.model.ConsumptionReading
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs

private fun List<Double>.median(): Double {
    val sorted = sorted()
    return if (size % 2 == 0) {
        (sorted[size / 2 - 1] + sorted[size / 2]) / 2
    } else {
        sorted[size / 2]
    }
}

class UsageAnalyticsImpl : UsageAnalytics {
    private val reports = ConcurrentHashMap<String, UsageReport>()
    private val patterns = ConcurrentHashMap<String, List<ConsumptionPattern>>()
    private val trends = ConcurrentHashMap<String, List<ConsumptionTrend>>()
    private val anomalyCallbacks = mutableListOf<(ConsumptionAnomaly) -> Unit>()
    private var monitoringActive = true

    override fun generateReport(period: AnalysisPeriod): UsageReport {
        val reportId = "REP-${UUID.randomUUID()}"
        val detectedPatterns = analyzePatterns(period)
        val detectedTrends = identifyTrends(period)
        val anomalies = detectAnomalies(detectedPatterns.flatMap { it.readings })

        val report = UsageReport(
            reportId = reportId,
            period = period,
            patterns = detectedPatterns,
            trends = detectedTrends,
            anomalies = anomalies,
            generatedAt = LocalDateTime.now()
        )

        reports[reportId] = report
        return report
    }

    override fun analyzePatterns(period: AnalysisPeriod): List<ConsumptionPattern> {
        val periodKey = "${period.startDate}_${period.endDate}"
        return patterns.computeIfAbsent(periodKey) {
            listOf(
                // Peak usage pattern
                createPattern(
                    type = PatternType.PEAK_USAGE,
                    description = "High consumption during business hours",
                    confidence = 0.85,
                    period = period,
                    isPeak = true
                ),
                // Off-peak usage pattern
                createPattern(
                    type = PatternType.OFF_PEAK_USAGE,
                    description = "Lower consumption during non-business hours",
                    confidence = 0.75,
                    period = period,
                    isPeak = false
                )
            )
        }
    }

    override fun identifyTrends(period: AnalysisPeriod): List<ConsumptionTrend> {
        val periodKey = "${period.startDate}_${period.endDate}"
        return trends.computeIfAbsent(periodKey) {
            listOf(
                ConsumptionTrend(
                    trendId = "TRD-${UUID.randomUUID()}",
                    direction = TrendDirection.INCREASING,
                    magnitude = 0.15,
                    period = period,
                    description = "Gradual increase in peak hour consumption"
                ),
                ConsumptionTrend(
                    trendId = "TRD-${UUID.randomUUID()}",
                    direction = TrendDirection.STABLE,
                    magnitude = 0.05,
                    period = period,
                    description = "Stable off-peak consumption"
                )
            )
        }
    }

    override fun detectAnomalies(readings: List<ConsumptionReading>): List<ConsumptionAnomaly> {
        if (readings.isEmpty()) return emptyList()

        val anomalies = mutableListOf<ConsumptionAnomaly>()
        // For small datasets, use minimum value as baseline
        val baselineConsumption = if (readings.size <= 2) {
            readings.minOf { it.kilowattHours }
        } else {
            readings.map { it.kilowattHours }.median()
        }
        val threshold = baselineConsumption * 1.5

        readings.forEach { reading ->
            if (reading.kilowattHours > threshold) {
                val ratio = reading.kilowattHours / baselineConsumption
                val severity = when {
                    ratio >= 3.0 -> AnomalySeverity.HIGH
                    ratio >= 2.0 -> AnomalySeverity.MEDIUM
                    else -> AnomalySeverity.LOW
                }
                val anomaly = ConsumptionAnomaly(
                    anomalyId = "ANM-${UUID.randomUUID()}",
                    severity = severity,
                    description = "Unusually high consumption detected",
                    detectedAt = LocalDateTime.now(),
                    affectedReadings = listOf(reading)
                )
                anomalies.add(anomaly)
                notifyAnomalyCallbacks(anomaly)
            }
        }

        return anomalies
    }

    override fun registerAnomalyCallback(callback: (ConsumptionAnomaly) -> Unit) {
        anomalyCallbacks.add(callback)
    }

    override fun isMonitoringActive(): Boolean = monitoringActive

    private fun createPattern(
        type: PatternType,
        description: String,
        confidence: Double,
        period: AnalysisPeriod,
        isPeak: Boolean
    ): ConsumptionPattern {
        val readings = generateSimulatedReadings(period, isPeak)
        return ConsumptionPattern(
            patternId = "PTN-${UUID.randomUUID()}",
            type = type,
            description = description,
            confidence = confidence,
            affectedCustomers = listOf("SIMULATED-CUSTOMER"),
            readings = readings
        )
    }

    private fun generateSimulatedReadings(period: AnalysisPeriod, isPeak: Boolean): List<ConsumptionReading> {
        val readings = mutableListOf<ConsumptionReading>()
        var currentTime = period.startDate

        while (!currentTime.isAfter(period.endDate)) {
            val baseUsage = if (isPeak) 2.5 else 1.0
            val variation = (Math.random() - 0.5) * 0.2 * baseUsage

            readings.add(ConsumptionReading(
                timestamp = currentTime,
                kilowattHours = baseUsage + variation,
                isPeakHours = isPeak
            ))

            currentTime = when (period.granularity) {
                TimeGranularity.HOURLY -> currentTime.plusHours(1)
                TimeGranularity.DAILY -> currentTime.plusDays(1)
                TimeGranularity.WEEKLY -> currentTime.plusWeeks(1)
                TimeGranularity.MONTHLY -> currentTime.plusMonths(1)
            }
        }

        return readings
    }

    private fun notifyAnomalyCallbacks(anomaly: ConsumptionAnomaly) {
        anomalyCallbacks.forEach { callback ->
            try {
                callback(anomaly)
            } catch (e: Exception) {
                // Log error but continue with other callbacks
                println("Error in anomaly callback: ${e.message}")
            }
        }
    }
}
