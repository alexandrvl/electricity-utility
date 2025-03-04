package com.electricity.utility.spec.contracts.steps

interface BillingSteps {
    /**
     * Sets up the context for a billing administrator
     * Requires successful authentication as a user with billing administrator role
     */
    fun givenIAmABillingAdministrator()

    /**
     * Sets up the context for a logged-in customer
     * Requires successful authentication as a user with customer role
     */
    fun givenIAmALoggedInCustomer()

    /**
     * Simulates initiating the monthly billing process
     */
    fun whenIInitiateMonthlyBilling()

    /**
     * Simulates accessing the billing section
     */
    fun whenIAccessMyBillingSection()

    /**
     * Verifies that bills are generated for all active accounts
     */
    fun thenBillsShouldBeGeneratedForAllActiveAccounts()

    /**
     * Verifies that bills include all consumption data
     */
    fun thenShouldIncludeAllConsumptionData()

    /**
     * Verifies that current bill is visible
     */
    fun thenIShouldSeeMyCurrentBill()

    /**
     * Verifies that payment history is visible
     */
    fun thenIShouldSeePaymentHistory()
}
