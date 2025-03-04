package com.electricity.utility.domain.billing.service

import com.electricity.utility.domain.authentication.repository.TestUserRepository
import com.electricity.utility.domain.billing.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

class BillingImplTest {
    private lateinit var userRepository: TestUserRepository
    private lateinit var billing: BillingImpl

    @BeforeEach
    fun setup() {
        userRepository = TestUserRepository()
        billing = BillingImpl(userRepository)
    }

    @Test
    fun `should generate monthly bills for active accounts`() {
        // Given
        val activeUser1 = userRepository.addTestUser("user1", "user1@test.com", true)
        val activeUser2 = userRepository.addTestUser("user2", "user2@test.com", true)
        userRepository.addTestUser("inactive", "inactive@test.com", false)

        // When
        val bills = billing.generateMonthlyBills()

        // Then
        assertEquals(2, bills.size)
        assertTrue(bills.any { it.customerId == activeUser1.username })
        assertTrue(bills.any { it.customerId == activeUser2.username })
        assertTrue(bills.all { it.status == BillStatus.GENERATED })
        assertTrue(bills.all { it.amount.signum() > 0 })
    }

    @Test
    fun `should get current bill for customer`() {
        // Given
        val user = userRepository.addTestUser("user1", "user1@test.com", true)
        billing.generateMonthlyBills()

        // When
        val currentBill = billing.getCurrentBill(user.username)

        // Then
        assertNotNull(currentBill)
        assertEquals(user.username, currentBill!!.customerId)
        assertEquals(BillStatus.GENERATED, currentBill.status)
    }

    @Test
    fun `should verify billing section accessibility`() {
        // Given
        val activeUser = userRepository.addTestUser("active", "active@test.com", true)
        val inactiveUser = userRepository.addTestUser("inactive", "inactive@test.com", false)

        // Then
        assertTrue(billing.isBillingSectionAccessible(activeUser.username))
        assertFalse(billing.isBillingSectionAccessible(inactiveUser.username))
    }

    @Test
    fun `should verify consumption data completeness`() {
        // Given
        val user = userRepository.addTestUser("user1", "user1@test.com", true)
        val currentPeriod = BillingPeriod(
            startDate = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0),
            endDate = LocalDateTime.now()
                .withDayOfMonth(LocalDateTime.now().month.length(LocalDateTime.now().toLocalDate().isLeapYear))
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
        )

        // When
        val isComplete = billing.isConsumptionDataComplete(user.username, currentPeriod)

        // Then
        assertTrue(isComplete)
    }
}