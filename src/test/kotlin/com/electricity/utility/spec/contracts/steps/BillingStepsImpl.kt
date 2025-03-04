package com.electricity.utility.spec.contracts.steps

import com.electricity.utility.spec.contracts.domain.Billing
import com.electricity.utility.spec.contracts.domain.Bill
import com.electricity.utility.spec.contracts.domain.Payment

class BillingStepsImpl(
    private val billing: Billing,
    private val authenticationContext: AuthenticationContext
) : BillingSteps {
    private var currentCustomerId: String? = null
    private var generatedBills: List<Bill>? = null

    override fun givenIAmABillingAdministrator() {
        require(authenticationContext.hasRole("BILLING_ADMIN")) {
            "User must have BILLING_ADMIN role"
        }
    }

    override fun givenIAmALoggedInCustomer() {
        require(authenticationContext.isAuthenticated()) {
            "User must be authenticated"
        }
        currentCustomerId = authenticationContext.getCurrentUserId()
        require(currentCustomerId != null) {
            "Failed to get current user ID"
        }
    }

    override fun whenIInitiateMonthlyBilling() {
        generatedBills = billing.generateMonthlyBills()
    }

    override fun whenIAccessMyBillingSection() {
        require(currentCustomerId != null) {
            "Customer ID not set"
        }
        require(billing.isBillingSectionAccessible(currentCustomerId!!)) {
            "Billing section is not accessible"
        }
    }

    override fun thenBillsShouldBeGeneratedForAllActiveAccounts() {
        require(generatedBills != null) {
            "Bills have not been generated"
        }
        val activeAccounts = billing.getActiveAccounts()
        require(generatedBills!!.map { it.customerId }.toSet() == activeAccounts.toSet()) {
            "Bills were not generated for all active accounts"
        }
    }

    override fun thenShouldIncludeAllConsumptionData() {
        require(generatedBills != null) {
            "Bills have not been generated"
        }
        generatedBills!!.forEach { bill ->
            require(billing.isConsumptionDataComplete(bill.customerId, bill.billingPeriod)) {
                "Consumption data is incomplete for customer ${bill.customerId}"
            }
        }
    }

    override fun thenIShouldSeeMyCurrentBill() {
        require(currentCustomerId != null) {
            "Customer ID not set"
        }
        val currentBill = billing.getCurrentBill(currentCustomerId!!)
        require(currentBill != null) {
            "Current bill not found"
        }
    }

    override fun thenIShouldSeePaymentHistory() {
        require(currentCustomerId != null) {
            "Customer ID not set"
        }
        val paymentHistory = billing.getPaymentHistory(currentCustomerId!!)
        require(paymentHistory.isNotEmpty()) {
            "Payment history is empty"
        }
    }
}
