package test.spec.contracts.steps

interface OnlinePaymentSteps {
    /**
     * Sets up the context for viewing a bill
     * Requires an existing bill for the customer
     */
    fun givenIAmViewingMyBill()

    /**
     * Simulates making a payment with a valid payment method
     */
    fun whenIMakeAPaymentUsingValidPaymentMethod()

    /**
     * Simulates making a payment with an invalid payment method
     */
    fun whenIMakeAPaymentUsingInvalidPaymentMethod()

    /**
     * Verifies that the payment was processed successfully
     */
    fun thenThePaymentShouldBeProcessed()

    /**
     * Verifies that a receipt was received
     */
    fun thenIShouldReceiveAReceipt()

    /**
     * Verifies that an error message is displayed
     */
    fun thenIShouldSeeAnErrorMessage()

    /**
     * Verifies that the bill remains unpaid
     */
    fun thenMyBillShouldRemainUnpaid()
}