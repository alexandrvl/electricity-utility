package com.electricity.utility.domain.payment.service

import com.electricity.utility.domain.payment.model.*
import com.electricity.utility.domain.billing.model.*
import com.electricity.utility.domain.billing.service.BillingImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicReference

class OnlinePaymentImplTest {
    private lateinit var billing: BillingImpl
    private lateinit var payment: OnlinePaymentImpl

    @BeforeEach
    fun setup() {
        billing = mockk(relaxed = true)
        payment = OnlinePaymentImpl(billing)
    }

    @Test
    fun `should process valid payment`() {
        // Given
        val billId = "BILL-123"
        val paymentMethod = PaymentMethod(
            methodId = "PM-123",
            type = PaymentMethodType.CREDIT_CARD,
            details = mapOf("cardNumber" to "****1234"),
            isValid = true
        )
        val amount = BigDecimal("100.00")

        every { billing.getCurrentBill(billId) } returns Bill(
            billId = billId,
            customerId = "CUST-123",
            billingPeriod = BillingPeriod(LocalDateTime.now(), LocalDateTime.now()),
            amount = amount,
            dueDate = LocalDateTime.now().plusDays(14),
            status = BillStatus.GENERATED,
            consumptionDetails = ConsumptionDetails(
                totalKilowattHours = 100.0,
                peakUsageKilowattHours = 60.0,
                offPeakUsageKilowattHours = 40.0,
                readings = emptyList()
            )
        )

        // When
        val result = payment.processPayment(billId, paymentMethod, amount)

        // Then
        assertTrue(result is PaymentResult.Success)
        val receipt = (result as PaymentResult.Success).receipt
        assertEquals(billId, receipt.billId)
        assertEquals(amount, receipt.amount)
        assertEquals(PaymentMethodType.CREDIT_CARD, receipt.paymentMethod)
        assertEquals(PaymentStatus.COMPLETED, receipt.status)
    }

    @Test
    fun `should fail for invalid payment method`() {
        // Given
        val billId = "BILL-123"
        val paymentMethod = PaymentMethod(
            methodId = "PM-123",
            type = PaymentMethodType.CREDIT_CARD,
            details = emptyMap(),
            isValid = false
        )
        val amount = BigDecimal("100.00")

        // When
        val result = payment.processPayment(billId, paymentMethod, amount)

        // Then
        assertTrue(result is PaymentResult.Failure)
        assertEquals("INVALID_PAYMENT_METHOD", (result as PaymentResult.Failure).errorCode)
    }

    @Test
    fun `should fail for already paid bill`() {
        // Given
        val billId = "BILL-123"
        val paymentMethod = PaymentMethod(
            methodId = "PM-123",
            type = PaymentMethodType.CREDIT_CARD,
            details = mapOf("cardNumber" to "****1234"),
            isValid = true
        )
        val amount = BigDecimal("100.00")

        every { billing.getCurrentBill(billId) } returns Bill(
            billId = billId,
            customerId = "CUST-123",
            billingPeriod = BillingPeriod(LocalDateTime.now(), LocalDateTime.now()),
            amount = amount,
            dueDate = LocalDateTime.now().plusDays(14),
            status = BillStatus.PAID,
            consumptionDetails = ConsumptionDetails(
                totalKilowattHours = 100.0,
                peakUsageKilowattHours = 60.0,
                offPeakUsageKilowattHours = 40.0,
                readings = emptyList()
            )
        )

        // When
        val result = payment.processPayment(billId, paymentMethod, amount)

        // Then
        assertTrue(result is PaymentResult.Failure)
        assertEquals("INVALID_BILL", (result as PaymentResult.Failure).errorCode)
    }

    @Test
    fun `should notify callbacks on payment status change`() {
        // Given
        val billId = "BILL-123"
        val paymentMethod = PaymentMethod(
            methodId = "PM-123",
            type = PaymentMethodType.CREDIT_CARD,
            details = mapOf("cardNumber" to "****1234"),
            isValid = true
        )
        val amount = BigDecimal("100.00")
        val statusUpdate = AtomicReference<PaymentStatus>()

        every { billing.getCurrentBill(billId) } returns Bill(
            billId = billId,
            customerId = "CUST-123",
            billingPeriod = BillingPeriod(LocalDateTime.now(), LocalDateTime.now()),
            amount = amount,
            dueDate = LocalDateTime.now().plusDays(14),
            status = BillStatus.GENERATED,
            consumptionDetails = ConsumptionDetails(
                totalKilowattHours = 100.0,
                peakUsageKilowattHours = 60.0,
                offPeakUsageKilowattHours = 40.0,
                readings = emptyList()
            )
        )

        // Register callback before processing payment
        payment.registerPaymentStatusCallback("any") { status ->
            statusUpdate.set(status)
        }

        // When
        val result = payment.processPayment(billId, paymentMethod, amount) as PaymentResult.Success

        // Then
        assertNotNull(statusUpdate.get())
        assertEquals(PaymentStatus.COMPLETED, statusUpdate.get())
    }

    @Test
    fun `should retrieve receipt for payment`() {
        // Given
        val billId = "BILL-123"
        val paymentMethod = PaymentMethod(
            methodId = "PM-123",
            type = PaymentMethodType.CREDIT_CARD,
            details = mapOf("cardNumber" to "****1234"),
            isValid = true
        )
        val amount = BigDecimal("100.00")

        every { billing.getCurrentBill(billId) } returns Bill(
            billId = billId,
            customerId = "CUST-123",
            billingPeriod = BillingPeriod(LocalDateTime.now(), LocalDateTime.now()),
            amount = amount,
            dueDate = LocalDateTime.now().plusDays(14),
            status = BillStatus.GENERATED,
            consumptionDetails = ConsumptionDetails(
                totalKilowattHours = 100.0,
                peakUsageKilowattHours = 60.0,
                offPeakUsageKilowattHours = 40.0,
                readings = emptyList()
            )
        )

        // When
        val result = payment.processPayment(billId, paymentMethod, amount) as PaymentResult.Success
        val receipt = payment.getReceipt(result.receipt.paymentId)

        // Then
        assertNotNull(receipt)
        assertEquals(billId, receipt!!.billId)
        assertEquals(amount, receipt.amount)
        assertEquals(PaymentMethodType.CREDIT_CARD, receipt.paymentMethod)
    }
}
