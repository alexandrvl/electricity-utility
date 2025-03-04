package com.electricity.utility.domain.analytics.model

import java.time.LocalDateTime
import com.electricity.utility.domain.billing.model.ConsumptionReading

/**
 * Represents a usage analysis report
 */
data class UsageReport(
    val reportId: String,
    val period: AnalysisPeriod,
    val patterns: List<ConsumptionPattern>,
    val trends: List<ConsumptionTrend>,
    val anomalies: List<ConsumptionAnomaly>,
    val generatedAt: LocalDateTime
)

/**
 * Represents a period for analysis
 */
data class AnalysisPeriod(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val granularity: TimeGranularity
)

/**
 * Represents a consumption pattern
 */
data class ConsumptionPattern(
    val patternId: String,
    val type: PatternType,
    val description: String,
    val confidence: Double,
    val affectedCustomers: List<String>,
    val readings: List<ConsumptionReading>
)

/**
 * Represents a consumption trend
 */
data class ConsumptionTrend(
    val trendId: String,
    val direction: TrendDirection,
    val magnitude: Double,
    val period: AnalysisPeriod,
    val description: String
)

/**
 * Represents a consumption anomaly
 */
data class ConsumptionAnomaly(
    val anomalyId: String,
    val severity: AnomalySeverity,
    val description: String,
    val detectedAt: LocalDateTime,
    val affectedReadings: List<ConsumptionReading>
)

/**
 * Defines time granularity for analysis
 */
enum class TimeGranularity {
    HOURLY,
    DAILY,
    WEEKLY,
    MONTHLY
}

/**
 * Defines types of consumption patterns
 */
enum class PatternType {
    CYCLICAL,
    SEASONAL,
    PEAK_USAGE,
    OFF_PEAK_USAGE,
    BASELINE
}

/**
 * Defines trend directions
 */
enum class TrendDirection {
    INCREASING,
    DECREASING,
    STABLE,
    FLUCTUATING
}

/**
 * Defines anomaly severity levels
 */
enum class AnomalySeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

/**
 * Contract for usage analytics operations
 */
interface UsageAnalytics {
    /**
     * Generates a usage analysis report
     * @param period The period to analyze
     * @return The generated report
     */
    fun generateReport(period: AnalysisPeriod): UsageReport

    /**
     * Analyzes consumption patterns
     * @param period The period to analyze
     * @return List of identified patterns
     */
    fun analyzePatterns(period: AnalysisPeriod): List<ConsumptionPattern>

    /**
     * Identifies consumption trends
     * @param period The period to analyze
     * @return List of identified trends
     */
    fun identifyTrends(period: AnalysisPeriod): List<ConsumptionTrend>

    /**
     * Detects consumption anomalies
     * @param readings The consumption readings to analyze
     * @return List of detected anomalies
     */
    fun detectAnomalies(readings: List<ConsumptionReading>): List<ConsumptionAnomaly>

    /**
     * Registers a callback for anomaly detection
     * @param callback The function to handle detected anomalies
     */
    fun registerAnomalyCallback(callback: (ConsumptionAnomaly) -> Unit)

    /**
     * Checks if monitoring is active
     * @return true if usage monitoring is active, false otherwise
     */
    fun isMonitoringActive(): Boolean
}