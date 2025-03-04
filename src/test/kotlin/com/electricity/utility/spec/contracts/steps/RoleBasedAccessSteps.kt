package test.spec.contracts.steps

interface RoleBasedAccessSteps {
    /**
     * Sets up the context for a system administrator
     * Requires successful authentication as a user with administrator role
     */
    fun givenIAmASystemAdministrator()

    /**
     * Sets up the context for a user with limited permissions
     * Requires successful authentication as a regular user
     */
    fun givenIAmAUserWithLimitedPermissions()

    /**
     * Simulates assigning a role to a user
     */
    fun whenIAssignARoleToAUser()

    /**
     * Simulates attempting to access restricted features
     */
    fun whenIAttemptToAccessRestrictedFeatures()

    /**
     * Verifies that the user has appropriate permissions
     */
    fun thenTheUserShouldHaveAppropriatePermissions()

    /**
     * Verifies that the user has access to relevant features
     */
    fun thenAccessToRelevantFeatures()

    /**
     * Verifies that access is denied
     */
    fun thenIShouldBeDeniedAccess()

    /**
     * Verifies that an appropriate message is displayed
     */
    fun thenSeeAnAppropriateMessage()
}