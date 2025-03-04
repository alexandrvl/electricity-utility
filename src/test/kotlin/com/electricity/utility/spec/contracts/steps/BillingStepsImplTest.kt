package com.electricity.utility.spec.contracts.steps

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import com.electricity.utility.spec.contracts.domain.*
import java.math.BigDecimal
import java.time.LocalDateTime

class BillingStepsImplTest {
    private val billing = mockk<Billing>()
    private val authContext = mockk<AuthenticationContext>()
    private lateinit var billingSteps: BillingStepsImpl

    @BeforeEach
    fun setup() {
        clearAllMocks()
        billingSteps = BillingStepsImpl(billing, authContext)
    }

    @Test
    fun `test successful monthly bill generation scenario`() {
        // Given
        every { authContext.hasRole("BILLING_ADMIN") } returns true

        val activeAccounts = listOf("customer1", "customer2")
        val bills = activeAccounts.map { customerId ->
            Bill(
                billId = "bill-$customerId",
                customerId = customerId,
                billingPeriod = BillingPeriod(
                    startDate = LocalDateTime.now().minusMonths(1),
                    endDate = LocalDateTime.now()
                ),
                amount = BigDecimal("100.00"),
                dueDate = LocalDateTime.now().plusDays(30),
                status = BillStatus.GENERATED,
                consumptionDetails = ConsumptionDetails(
                    totalKilowattHours = 100.0,
                    peakUsageKilowattHours = 60.0,
                    offPeakUsageKilowattHours = 40.0,
                    readings = emptyList()
                )
            )
        }

        every { billing.generateMonthlyBills() } returns bills
        every { billing.getActiveAccounts() } returns activeAccounts
        every { billing.isConsumptionDataComplete(any(), any()) } returns true

        // When & Then
        billingSteps.givenIAmABillingAdministrator()
        billingSteps.whenIInitiateMonthlyBilling()
        billingSteps.thenBillsShouldBeGeneratedForAllActiveAccounts()
        billingSteps.thenShouldIncludeAllConsumptionData()

        verify(exactly = 1) { billing.generateMonthlyBills() }
        verify(exactly = 1) { billing.getActiveAccounts() }
        verify(exactly = 2) { billing.isConsumptionDataComplete(any(), any()) }
    }

    @Test
    fun `test successful bill viewing scenario`() {
        // Given
        val customerId = "customer1"
        val currentBill = Bill(
            billId = "bill-1",
            customerId = customerId,
            billingPeriod = BillingPeriod(
                startDate = LocalDateTime.now().minusMonths(1),
                endDate = LocalDateTime.now()
            ),
            amount = BigDecimal("100.00"),
            dueDate = LocalDateTime.now().plusDays(30),
            status = BillStatus.GENERATED,
            consumptionDetails = ConsumptionDetails(
                totalKilowattHours = 100.0,
                peakUsageKilowattHours = 60.0,
                offPeakUsageKilowattHours = 40.0,
                readings = emptyList()
            )
        )
        val payments = listOf(
            Payment(
                paymentId = "payment-1",
                billId = "bill-1",
                amount = BigDecimal("100.00"),
                timestamp = LocalDateTime.now(),
                status = PaymentStatus.COMPLETED
            )
        )

        every { authContext.isAuthenticated() } returns true
        every { authContext.getCurrentUserId() } returns customerId
        every { billing.isBillingSectionAccessible(customerId) } returns true
        every { billing.getCurrentBill(customerId) } returns currentBill
        every { billing.getPaymentHistory(customerId) } returns payments

        // When & Then
        billingSteps.givenIAmALoggedInCustomer()
        billingSteps.whenIAccessMyBillingSection()
        billingSteps.thenIShouldSeeMyCurrentBill()
        billingSteps.thenIShouldSeePaymentHistory()

        verify(exactly = 1) { billing.isBillingSectionAccessible(customerId) }
        verify(exactly = 1) { billing.getCurrentBill(customerId) }
        verify(exactly = 1) { billing.getPaymentHistory(customerId) }
    }

    @Test
    fun `test unauthorized access to billing admin functions`() {
        // Given
        every { authContext.hasRole("BILLING_ADMIN") } returns false

        // When & Then
        assertThrows<IllegalArgumentException> {
            billingSteps.givenIAmABillingAdministrator()
        }
    }

    @Test
    fun `test unauthenticated access to customer functions`() {
        // Given
        every { authContext.isAuthenticated() } returns false

        // When & Then
        assertThrows<IllegalArgumentException> {
            billingSteps.givenIAmALoggedInCustomer()
        }
    }
}
