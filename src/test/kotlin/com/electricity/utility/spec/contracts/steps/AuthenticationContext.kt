package com.electricity.utility.spec.contracts.steps

/**
 * Provides authentication and authorization context for test steps
 */
interface AuthenticationContext {
    /**
     * Checks if the current user has the specified role
     * @param role The role to check
     * @return true if the user has the role, false otherwise
     */
    fun hasRole(role: String): Boolean

    /**
     * Checks if there is an authenticated user in the current context
     * @return true if a user is authenticated, false otherwise
     */
    fun isAuthenticated(): Boolean

    /**
     * Gets the ID of the currently authenticated user
     * @return The user ID if authenticated, null otherwise
     */
    fun getCurrentUserId(): String?
}
