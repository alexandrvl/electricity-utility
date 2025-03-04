package test.spec.contracts.steps

interface GridMonitoringSteps {
    /**
     * Sets up the context for a logged-in grid operator
     * Requires successful authentication as a user with grid operator role
     */
    fun givenIAmALoggedInGridOperator()

    /**
     * Simulates accessing the grid monitoring dashboard
     */
    fun whenIAccessTheGridMonitoringDashboard()

    /**
     * Simulates triggering a grid alert in the system
     */
    fun whenAGridAlertIsTriggered()

    /**
     * Verifies that real-time grid status is visible
     */
    fun thenIShouldSeeRealTimeGridStatus()

    /**
     * Verifies that active alerts are visible
     */
    fun thenIShouldSeeAnyActiveAlerts()

    /**
     * Verifies that an immediate notification is received
     */
    fun thenIShouldReceiveImmediateNotification()

    /**
     * Verifies that detailed alert information is visible
     */
    fun thenIShouldSeeDetailedAlertInformation()
}