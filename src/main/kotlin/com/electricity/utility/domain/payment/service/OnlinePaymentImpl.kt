package com.electricity.utility.domain.payment.service

import com.electricity.utility.domain.payment.model.*
import com.electricity.utility.domain.billing.model.PaymentStatus
import com.electricity.utility.domain.billing.model.BillStatus
import com.electricity.utility.domain.billing.service.BillingImpl
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class OnlinePaymentImpl(
    private val billing: BillingImpl
) : OnlinePayment {
    private val receipts = ConcurrentHashMap<String, PaymentReceipt>()
    private val statusCallbacks = mutableListOf<(PaymentStatus) -> Unit>()

    override fun processPayment(billId: String, paymentMethod: PaymentMethod, amount: BigDecimal): PaymentResult {
        if (!isBillPayable(billId)) {
            return PaymentResult.Failure("INVALID_BILL", "Bill is not payable")
        }

        if (!validatePaymentMethod(paymentMethod)) {
            return PaymentResult.Failure("INVALID_PAYMENT_METHOD", "Payment method is invalid")
        }

        val paymentId = "PAY-${UUID.randomUUID()}"
        val receipt = PaymentReceipt(
            receiptId = "RCP-${UUID.randomUUID()}",
            paymentId = paymentId,
            billId = billId,
            amount = amount,
            paymentMethod = paymentMethod.type,
            timestamp = LocalDateTime.now(),
            status = PaymentStatus.COMPLETED,
            transactionDetails = mapOf(
                "processor" to "INTERNAL",
                "timestamp" to LocalDateTime.now().toString()
            )
        )

        receipts[paymentId] = receipt
        notifyStatusCallbacks(paymentId, PaymentStatus.COMPLETED)
        return PaymentResult.Success(receipt)
    }

    override fun validatePaymentMethod(paymentMethod: PaymentMethod): Boolean {
        return paymentMethod.isValid && paymentMethod.details.isNotEmpty()
    }

    override fun getReceipt(paymentId: String): PaymentReceipt? {
        return receipts[paymentId]
    }

    override fun isBillPayable(billId: String): Boolean {
        return billing.getCurrentBill(billId)?.status != BillStatus.PAID
    }

    override fun getPaymentStatus(paymentId: String): PaymentStatus? {
        return receipts[paymentId]?.status
    }

    override fun registerPaymentStatusCallback(paymentId: String, callback: (PaymentStatus) -> Unit) {
        statusCallbacks.add(callback)
    }

    private fun notifyStatusCallbacks(paymentId: String, status: PaymentStatus) {
        statusCallbacks.forEach { callback ->
            try {
                callback(status)
            } catch (e: Exception) {
                // Log error but continue with other callbacks
                println("Error in payment status callback: ${e.message}")
            }
        }
    }
}
