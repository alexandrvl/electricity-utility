package com.electricity.utility.domain.billing.model

import java.time.LocalDateTime
import java.math.BigDecimal

/**
 * Represents a customer bill
 */
data class Bill(
    val billId: String,
    val customerId: String,
    val billingPeriod: BillingPeriod,
    val amount: BigDecimal,
    val dueDate: LocalDateTime,
    val status: BillStatus,
    val consumptionDetails: ConsumptionDetails
)

/**
 * Represents a billing period
 */
data class BillingPeriod(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)

/**
 * Represents a consumption reading
 */
data class ConsumptionReading(
    val timestamp: LocalDateTime,
    val kilowattHours: Double,
    val isPeakHours: Boolean
)

/**
 * Represents consumption details for billing
 */
data class ConsumptionDetails(
    val totalKilowattHours: Double,
    val peakUsageKilowattHours: Double,
    val offPeakUsageKilowattHours: Double,
    val readings: List<ConsumptionReading>
)

/**
 * Represents a payment transaction
 */
data class Payment(
    val paymentId: String,
    val billId: String,
    val amount: BigDecimal,
    val timestamp: LocalDateTime,
    val status: PaymentStatus
)

/**
 * Defines possible bill statuses
 */
enum class BillStatus {
    PENDING,
    GENERATED,
    SENT,
    PAID,
    OVERDUE
}

/**
 * Defines possible payment statuses
 */
enum class PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED
}

/**
 * Contract for billing operations
 */
interface Billing {
    /**
     * Generates monthly bills for all active accounts
     * @return List of generated bills
     */
    fun generateMonthlyBills(): List<Bill>

    /**
     * Gets the current bill for a customer
     * @param customerId The ID of the customer
     * @return The current bill if available
     */
    fun getCurrentBill(customerId: String): Bill?

    /**
     * Gets payment history for a customer
     * @param customerId The ID of the customer
     * @return List of historical payments
     */
    fun getPaymentHistory(customerId: String): List<Payment>

    /**
     * Checks if billing section is accessible for a customer
     * @param customerId The ID of the customer
     * @return true if billing section can be accessed, false otherwise
     */
    fun isBillingSectionAccessible(customerId: String): Boolean

    /**
     * Gets list of active accounts for billing
     * @return List of customer IDs with active accounts
     */
    fun getActiveAccounts(): List<String>

    /**
     * Verifies if consumption data is complete for billing
     * @param customerId The ID of the customer
     * @param period The billing period to check
     * @return true if all consumption data is available, false otherwise
     */
    fun isConsumptionDataComplete(customerId: String, period: BillingPeriod): Boolean
}