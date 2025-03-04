package com.electricity.utility.spec.contracts.steps

interface DataSecuritySteps {
    /**
     * Sets up the context for data transmission
     * Requires sensitive data ready for transmission
     */
    fun givenSensitiveDataIsBeingTransmitted()

    /**
     * Sets up the context for sensitive operations
     * Requires authenticated user performing sensitive operation
     */
    fun givenAUserPerformsSensitiveOperations()

    /**
     * Simulates data being in transit
     */
    fun whenTheDataIsInTransit()

    /**
     * Simulates completion of a sensitive operation
     */
    fun whenTheOperationIsCompleted()

    /**
     * Verifies that data is encrypted
     */
    fun thenItShouldBeEncrypted()

    /**
     * Verifies that data is secure from unauthorized access
     */
    fun thenSecureFromUnauthorizedAccess()

    /**
     * Verifies that operation is logged in audit trail
     */
    fun thenItShouldBeLoggedInAuditTrail()

    /**
     * Verifies that operation details are included in log
     */
    fun thenIncludeRelevantOperationDetails()
}
