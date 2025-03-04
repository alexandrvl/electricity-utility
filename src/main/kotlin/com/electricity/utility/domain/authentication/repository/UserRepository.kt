package com.electricity.utility.domain.authentication.repository

import com.electricity.utility.domain.authentication.model.User

interface UserRepository {
    fun findByUsername(username: String): User?
    fun save(user: User): User
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}