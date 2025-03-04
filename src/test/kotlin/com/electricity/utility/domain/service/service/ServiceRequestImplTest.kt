package com.electricity.utility.domain.service.service

import com.electricity.utility.domain.authentication.repository.TestUserRepository
import com.electricity.utility.domain.service.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.atomic.AtomicReference

class ServiceRequestImplTest {
    private lateinit var userRepository: TestUserRepository
    private lateinit var serviceRequest: ServiceRequestImpl

    @BeforeEach
    fun setup() {
        userRepository = TestUserRepository()
        serviceRequest = ServiceRequestImpl(userRepository)
    }

    @Test
    fun `should create service request for active customer`() {
        // Given
        val user = userRepository.addTestUser("user1", "user1@test.com", true)

        // When
        val request = serviceRequest.createRequest(
            user.username,
            RequestType.TECHNICAL_SUPPORT,
            "Test request"
        )

        // Then
        assertNotNull(request)
        assertEquals(user.username, request.customerId)
        assertEquals(RequestType.TECHNICAL_SUPPORT, request.type)
        assertEquals(RequestStatus.SUBMITTED, request.status)
        assertEquals("Test request", request.description)
        assertNotNull(request.updates)
        assertTrue(request.updates.isNotEmpty())
    }

    @Test
    fun `should not create request for inactive customer`() {
        // Given
        val user = userRepository.addTestUser("inactive", "inactive@test.com", false)

        // Then
        assertThrows(IllegalArgumentException::class.java) {
            serviceRequest.createRequest(
                user.username,
                RequestType.TECHNICAL_SUPPORT,
                "Test request"
            )
        }
    }

    @Test
    fun `should get customer requests`() {
        // Given
        val user = userRepository.addTestUser("user1", "user1@test.com", true)
        val request1 = serviceRequest.createRequest(user.username, RequestType.TECHNICAL_SUPPORT, "Request 1")
        val request2 = serviceRequest.createRequest(user.username, RequestType.BILLING_INQUIRY, "Request 2")

        // When
        val requests = serviceRequest.getCustomerRequests(user.username)

        // Then
        assertEquals(2, requests.size)
        assertTrue(requests.any { it.requestId == request1.requestId })
        assertTrue(requests.any { it.requestId == request2.requestId })
    }

    @Test
    fun `should verify request ownership`() {
        // Given
        val user1 = userRepository.addTestUser("user1", "user1@test.com", true)
        val user2 = userRepository.addTestUser("user2", "user2@test.com", true)
        val request = serviceRequest.createRequest(user1.username, RequestType.TECHNICAL_SUPPORT, "Test request")

        // Then
        assertTrue(serviceRequest.verifyRequestOwnership(request.requestId, user1.username))
        assertFalse(serviceRequest.verifyRequestOwnership(request.requestId, user2.username))
    }

    @Test
    fun `should update request status and message`() {
        // Given
        val user = userRepository.addTestUser("user1", "user1@test.com", true)
        val request = serviceRequest.createRequest(user.username, RequestType.TECHNICAL_SUPPORT, "Test request")

        // When
        val update = serviceRequest.updateRequest(
            request.requestId,
            "Request is being processed",
            RequestStatus.IN_PROGRESS,
            "OPERATOR"
        )

        // Then
        val updatedRequest = serviceRequest.getRequest(request.requestId)
        assertNotNull(updatedRequest)
        assertEquals(RequestStatus.IN_PROGRESS, updatedRequest!!.status)
        assertTrue(updatedRequest.updates.any { it.message == "Request is being processed" })
        assertEquals("OPERATOR", update.updatedBy)
    }

    @Test
    fun `should throw exception when updating non-existent request`() {
        // Then
        assertThrows(IllegalArgumentException::class.java) {
            serviceRequest.updateRequest(
                "non-existent-id",
                "Update message",
                RequestStatus.IN_PROGRESS,
                "SYSTEM"
            )
        }
    }

    @Test
    fun `should notify callbacks on status updates`() {
        // Given
        val user = userRepository.addTestUser("user1", "user1@test.com", true)
        val request = serviceRequest.createRequest(user.username, RequestType.TECHNICAL_SUPPORT, "Test request")
        val lastUpdate = AtomicReference<RequestUpdate>()

        // When
        serviceRequest.registerForUpdates(request.requestId) { update ->
            lastUpdate.set(update)
        }

        // Add new update
        serviceRequest.updateRequest(request.requestId, "Processing request", RequestStatus.IN_PROGRESS, "SYSTEM")

        // Then
        assertNotNull(lastUpdate.get())
        assertEquals(RequestStatus.IN_PROGRESS, lastUpdate.get().status)
        assertEquals("Processing request", lastUpdate.get().message)
    }
}
