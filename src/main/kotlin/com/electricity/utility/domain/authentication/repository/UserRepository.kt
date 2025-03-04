package com.electricity.utility.domain.authentication.repository

import com.electricity.utility.domain.authentication.model.User

interface UserRepository {
    fun findByUsername(username: String): User?
    fun save(user: User): User
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean

    /**
     * Checks if a user is active based on their account status
     * @param userId The ID of the user to check
     * @return true if the user exists and is active, false otherwise
     */
    fun isUserActive(userId: String): Boolean

    /**
     * Gets a list of all active user IDs
     * @return List of user IDs with active accounts
     */
    fun getActiveUsers(): List<String>
}
