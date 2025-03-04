package test.spec.contracts.steps

interface ServiceRequestSteps {
    /**
     * Sets up the context for a logged-in customer
     * Requires successful authentication as a user with customer role
     */
    fun givenIAmALoggedInCustomer()

    /**
     * Sets up the context with an existing service request
     * Requires a previously submitted service request
     */
    fun givenIHaveSubmittedAServiceRequest()

    /**
     * Simulates submitting a new service request
     */
    fun whenISubmitAServiceRequest()

    /**
     * Simulates checking the status of a service request
     */
    fun whenICheckTheRequestStatus()

    /**
     * Verifies that the service request was recorded in the system
     */
    fun thenTheRequestShouldBeRecorded()

    /**
     * Verifies that a confirmation was received
     */
    fun thenIShouldReceiveAConfirmation()

    /**
     * Verifies that the current status is visible
     */
    fun thenIShouldSeeTheCurrentStatus()

    /**
     * Verifies that request updates are visible
     */
    fun thenAnyUpdatesToMyRequest()
}