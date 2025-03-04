package test.spec.contracts.steps

interface UsageAnalyticsSteps {
    /**
     * Sets up the context for a system analyst
     * Requires successful authentication as a user with system analyst role
     */
    fun givenIAmASystemAnalyst()

    /**
     * Sets up the context for monitoring usage patterns
     * Requires system analyst role and active monitoring session
     */
    fun givenIAmMonitoringUsagePatterns()

    /**
     * Simulates requesting a usage analysis report
     */
    fun whenIRequestAUsageAnalysisReport()

    /**
     * Simulates the occurrence of an unusual consumption pattern
     */
    fun whenAnUnusualConsumptionPatternOccurs()

    /**
     * Verifies that the system generates the requested report
     */
    fun thenTheSystemShouldGenerateTheReport()

    /**
     * Verifies that consumption patterns and trends are displayed
     */
    fun thenShowConsumptionPatternsAndTrends()

    /**
     * Verifies that the system flags an anomaly
     */
    fun thenTheSystemShouldFlagTheAnomaly()

    /**
     * Verifies that an alert is generated for investigation
     */
    fun thenGenerateAnAlertForInvestigation()
}