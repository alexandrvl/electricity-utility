package com.electricity.utility.spec.contracts.steps

interface UserAuthenticationSteps {
    /**
     * Sets up the context for a registered user
     */
    fun givenIAmARegisteredUser()

    /**
     * Sets up the context for any user (registered or not)
     */
    fun givenIAmAUser()

    /**
     * Simulates entering valid credentials
     */
    fun whenIEnterValidCredentials()

    /**
     * Simulates entering invalid credentials
     */
    fun whenIEnterInvalidCredentials()

    /**
     * Verifies that user is successfully logged into the system
     */
    fun thenIShouldBeLoggedIntoTheSystem()

    /**
     * Verifies that user's dashboard is displayed
     */
    fun thenIShouldSeeMyDashboard()

    /**
     * Verifies that an error message is displayed
     */
    fun thenIShouldSeeAnErrorMessage()

    /**
     * Verifies that user remains on the login page
     */
    fun thenIShouldRemainOnTheLoginPage()
}
