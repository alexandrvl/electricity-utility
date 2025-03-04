package com.electricity.utility.spec.contracts.domain

import java.math.BigDecimal
import java.time.LocalDateTime
import com.electricity.utility.spec.contracts.domain.PaymentStatus

/**
 * Represents a payment method
 */
data class PaymentMethod(
    val methodId: String,
    val type: PaymentMethodType,
    val details: Map<String, String>,
    val isValid: Boolean
)

/**
 * Represents a payment receipt
 */
data class PaymentReceipt(
    val receiptId: String,
    val paymentId: String,
    val billId: String,
    val amount: BigDecimal,
    val paymentMethod: PaymentMethodType,
    val timestamp: LocalDateTime,
    val status: PaymentStatus,
    val transactionDetails: Map<String, String>
)

/**
 * Defines types of payment methods
 */
enum class PaymentMethodType {
    CREDIT_CARD,
    DEBIT_CARD,
    BANK_TRANSFER,
    DIGITAL_WALLET
}

/**
 * Represents the result of a payment attempt
 */
sealed class PaymentResult {
    data class Success(val receipt: PaymentReceipt) : PaymentResult()
    data class Failure(val errorCode: String, val errorMessage: String) : PaymentResult()
}

/**
 * Contract for online payment operations
 */
interface OnlinePayment {
    /**
     * Processes a payment for a bill
     * @param billId The ID of the bill to pay
     * @param paymentMethod The payment method to use
     * @param amount The amount to pay
     * @return The result of the payment attempt
     */
    fun processPayment(billId: String, paymentMethod: PaymentMethod, amount: BigDecimal): PaymentResult

    /**
     * Validates a payment method
     * @param paymentMethod The payment method to validate
     * @return true if the payment method is valid, false otherwise
     */
    fun validatePaymentMethod(paymentMethod: PaymentMethod): Boolean

    /**
     * Gets a receipt for a payment
     * @param paymentId The ID of the payment
     * @return The receipt if found
     */
    fun getReceipt(paymentId: String): PaymentReceipt?

    /**
     * Checks if a bill can be paid
     * @param billId The ID of the bill
     * @return true if the bill exists and is unpaid, false otherwise
     */
    fun isBillPayable(billId: String): Boolean

    /**
     * Gets the payment status
     * @param paymentId The ID of the payment
     * @return The current status of the payment
     */
    fun getPaymentStatus(paymentId: String): PaymentStatus?

    /**
     * Registers a callback for payment status updates
     * @param paymentId The ID of the payment
     * @param callback The function to handle status updates
     */
    fun registerPaymentStatusCallback(paymentId: String, callback: (PaymentStatus) -> Unit)
}
