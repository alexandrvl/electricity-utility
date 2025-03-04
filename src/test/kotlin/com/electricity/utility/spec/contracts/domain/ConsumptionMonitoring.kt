package test.spec.contracts.domain

import java.time.LocalDateTime

/**
 * Represents a single consumption reading
 */
data class ConsumptionReading(
    val customerId: String,
    val timestamp: LocalDateTime,
    val kilowattHours: Double,
    val instantaneousDemand: Double
)

/**
 * Represents historical usage data for a time period
 */
data class UsagePattern(
    val period: String,  // e.g., "DAILY", "WEEKLY", "MONTHLY"
    val averageConsumption: Double,
    val peakConsumption: Double,
    val lowConsumption: Double,
    val totalConsumption: Double,
    val readings: List<ConsumptionReading>
)

/**
 * Contract for consumption monitoring operations
 */
interface ConsumptionMonitoring {
    /**
     * Gets the current consumption reading for a customer
     * @param customerId The ID of the customer
     * @return The latest consumption reading
     */
    fun getCurrentConsumption(customerId: String): ConsumptionReading

    /**
     * Gets historical usage patterns for a customer
     * @param customerId The ID of the customer
     * @param period The time period for analysis
     * @return The usage pattern for the specified period
     */
    fun getHistoricalUsage(customerId: String, period: String): UsagePattern

    /**
     * Registers a callback for real-time consumption updates
     * @param customerId The ID of the customer
     * @param callback The function to handle new readings
     */
    fun registerForUpdates(customerId: String, callback: (ConsumptionReading) -> Unit)

    /**
     * Checks if new consumption data is available
     * @param customerId The ID of the customer
     * @param lastReadingTime The timestamp of the last reading
     * @return true if newer data is available, false otherwise
     */
    fun hasNewData(customerId: String, lastReadingTime: LocalDateTime): Boolean

    /**
     * Checks if the consumption dashboard is accessible
     * @param customerId The ID of the customer
     * @return true if the dashboard can be accessed, false otherwise
     */
    fun isDashboardAccessible(customerId: String): Boolean
}