package com.electricity.utility.spec.contracts.domain

/**
 * Represents user credentials for authentication
 */
data class Credentials(
    val username: String,
    val password: String
)

/**
 * Represents the result of an authentication attempt
 */
sealed class AuthenticationResult {
    data class Success(val userId: String) : AuthenticationResult()
    data class Failure(val errorMessage: String) : AuthenticationResult()
}

/**
 * Contract for user authentication operations
 */
interface UserAuthentication {
    /**
     * Authenticates a user with the provided credentials
     * @param credentials The user's credentials
     * @return AuthenticationResult indicating success or failure
     */
    fun authenticate(credentials: Credentials): AuthenticationResult

    /**
     * Checks if a user is registered in the system
     * @param username The username to check
     * @return true if the user is registered, false otherwise
     */
    fun isUserRegistered(username: String): Boolean

    /**
     * Gets the current authentication status
     * @return true if a user is currently authenticated, false otherwise
     */
    fun isAuthenticated(): Boolean

    /**
     * Gets the current page/view the user is on
     * @return String identifier of the current page/view
     */
    fun getCurrentPage(): String
}
