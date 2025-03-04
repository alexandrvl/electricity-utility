package test.spec.contracts.steps

interface ConsumptionMonitoringSteps {
    /**
     * Sets up the context for a logged-in customer
     * Requires successful authentication as a user with customer role
     */
    fun givenIAmALoggedInCustomer()

    /**
     * Sets up the context for a customer viewing their consumption dashboard
     * Requires successful authentication and dashboard access
     */
    fun givenIAmViewingMyConsumptionDashboard()

    /**
     * Simulates accessing the consumption dashboard
     */
    fun whenIAccessMyConsumptionDashboard()

    /**
     * Simulates new consumption data becoming available
     */
    fun whenNewConsumptionDataIsAvailable()

    /**
     * Verifies that current consumption data is visible
     */
    fun thenIShouldSeeMyCurrentConsumption()

    /**
     * Verifies that historical usage patterns are visible
     */
    fun thenIShouldSeeHistoricalUsagePatterns()

    /**
     * Verifies that the dashboard updates automatically
     */
    fun thenTheDashboardShouldUpdateAutomatically()

    /**
     * Verifies that the latest readings are displayed
     */
    fun thenShowTheLatestReadings()
}