package com.electricity.utility.spec.contracts.domain

import java.time.LocalDateTime

/**
 * Represents a customer service request
 */
data class ServiceRequestData(
    val requestId: String,
    val customerId: String,
    val type: RequestType,
    val description: String,
    val status: RequestStatus,
    val createdAt: LocalDateTime,
    val updates: List<RequestUpdate>
)

/**
 * Represents an update to a service request
 */
data class RequestUpdate(
    val updateId: String,
    val requestId: String,
    val status: RequestStatus,
    val message: String,
    val timestamp: LocalDateTime,
    val updatedBy: String
)

/**
 * Defines types of service requests
 */
enum class RequestType {
    TECHNICAL_SUPPORT,
    BILLING_INQUIRY,
    SERVICE_INTERRUPTION,
    METER_ISSUE,
    GENERAL_INQUIRY
}

/**
 * Defines possible statuses for service requests
 */
enum class RequestStatus {
    SUBMITTED,
    RECEIVED,
    IN_PROGRESS,
    PENDING_CUSTOMER,
    RESOLVED,
    CLOSED
}

/**
 * Contract for service request operations
 */
interface ServiceRequest {
    /**
     * Creates a new service request
     * @param customerId The ID of the customer
     * @param type The type of service request
     * @param description The description of the request
     * @return The created service request
     */
    fun createRequest(customerId: String, type: RequestType, description: String): ServiceRequestData

    /**
     * Gets a service request by ID
     * @param requestId The ID of the request
     * @return The service request if found
     */
    fun getRequest(requestId: String): ServiceRequestData?

    /**
     * Gets all service requests for a customer
     * @param customerId The ID of the customer
     * @return List of customer's service requests
     */
    fun getCustomerRequests(customerId: String): List<ServiceRequestData>

    /**
     * Gets the current status of a request
     * @param requestId The ID of the request
     * @return The current status if request exists
     */
    fun getRequestStatus(requestId: String): RequestStatus?

    /**
     * Gets updates for a service request
     * @param requestId The ID of the request
     * @return List of updates for the request
     */
    fun getRequestUpdates(requestId: String): List<RequestUpdate>

    /**
     * Registers a callback for request status updates
     * @param requestId The ID of the request
     * @param callback The function to handle status updates
     */
    fun registerForUpdates(requestId: String, callback: (RequestUpdate) -> Unit)

    /**
     * Verifies if a request exists and belongs to the customer
     * @param requestId The ID of the request
     * @param customerId The ID of the customer
     * @return true if request exists and belongs to customer, false otherwise
     */
    fun verifyRequestOwnership(requestId: String, customerId: String): Boolean
}
