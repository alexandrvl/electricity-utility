package com.electricity.utility.infrastructure.authentication.repository

import com.electricity.utility.domain.authentication.model.User
import com.electricity.utility.domain.authentication.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryUserRepository(
    passwordEncoder: PasswordEncoder
) : UserRepository {

    private val users = ConcurrentHashMap<String, User>()

    init {
        // Add test users
        save(User(
            username = "admin",
            password = passwordEncoder.encode("admin123"),
            email = "admin@electricity.utility",
            roles = setOf("ROLE_ADMIN", "ROLE_USER")
        ))
        save(User(
            username = "user",
            password = passwordEncoder.encode("user123"),
            email = "user@electricity.utility",
            roles = setOf("ROLE_USER")
        ))
        save(User(
            username = "operator",
            password = passwordEncoder.encode("operator123"),
            email = "operator@electricity.utility",
            roles = setOf("ROLE_OPERATOR", "ROLE_USER")
        ))
    }

    override fun findByUsername(username: String): User? = users[username]

    override fun save(user: User): User {
        users[user.username] = user
        return user
    }

    override fun existsByUsername(username: String): Boolean = users.containsKey(username)

    override fun existsByEmail(email: String): Boolean = users.values.any { it.email == email }
}