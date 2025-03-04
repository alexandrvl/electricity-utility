package com.electricity.utility.domain.authentication.repository

import com.electricity.utility.domain.authentication.model.User

class TestUserRepository : UserRepository {
    private val users = mutableMapOf<String, User>()

    override fun findByUsername(username: String): User? = users.values.find { it.username == username }

    override fun save(user: User): User {
        users[user.username] = user
        return user
    }

    override fun existsByUsername(username: String): Boolean = users.containsKey(username)

    override fun existsByEmail(email: String): Boolean = users.values.any { it.email == email }

    override fun isUserActive(userId: String): Boolean {
        val user = users[userId]
        return user != null && 
               user.isEnabled() && 
               user.isAccountNonExpired() && 
               user.isAccountNonLocked() && 
               user.isCredentialsNonExpired()
    }

    override fun getActiveUsers(): List<String> {
        return users.values
            .filter { user -> 
                user.isEnabled() && 
                user.isAccountNonExpired() && 
                user.isAccountNonLocked() && 
                user.isCredentialsNonExpired() 
            }
            .map { it.username }
    }

    // Test helper methods
    fun addTestUser(username: String, email: String, isActive: Boolean = true): User {
        val user = User(
            username = username,
            password = "password",
            email = email,
            roles = setOf("ROLE_USER"),
            isEnabled = isActive,
            isAccountNonExpired = isActive,
            isAccountNonLocked = isActive,
            isCredentialsNonExpired = isActive
        )
        return save(user)
    }

    fun clear() {
        users.clear()
    }
}