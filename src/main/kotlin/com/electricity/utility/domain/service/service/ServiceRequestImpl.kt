package com.electricity.utility.domain.service.service

import com.electricity.utility.domain.service.model.*
import com.electricity.utility.domain.authentication.repository.UserRepository
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class ServiceRequestImpl(
    private val userRepository: UserRepository
) : ServiceRequest {
    private val requests = ConcurrentHashMap<String, ServiceRequestData>()
    private val updates = ConcurrentHashMap<String, MutableList<RequestUpdate>>()
    private val statusCallbacks = ConcurrentHashMap<String, MutableList<(RequestUpdate) -> Unit>>()

    override fun createRequest(customerId: String, type: RequestType, description: String): ServiceRequestData {
        require(userRepository.isUserActive(customerId)) { "Customer must be active to create requests" }

        val requestId = "REQ-${UUID.randomUUID()}"
        val request = ServiceRequestData(
            requestId = requestId,
            customerId = customerId,
            type = type,
            description = description,
            status = RequestStatus.SUBMITTED,
            createdAt = LocalDateTime.now(),
            updates = emptyList()
        )

        requests[requestId] = request
        updates[requestId] = mutableListOf()

        // Create initial update
        addUpdate(requestId, "Request created", RequestStatus.SUBMITTED, "SYSTEM")

        // Return request with updates
        return getRequest(requestId)!!
    }

    override fun getRequest(requestId: String): ServiceRequestData? {
        val request = requests[requestId]
        return request?.copy(updates = updates[requestId] ?: emptyList())
    }

    override fun getCustomerRequests(customerId: String): List<ServiceRequestData> {
        return requests.values
            .filter { it.customerId == customerId }
            .map { request -> 
                request.copy(updates = updates[request.requestId] ?: emptyList())
            }
    }

    override fun getRequestStatus(requestId: String): RequestStatus? {
        return requests[requestId]?.status
    }

    override fun getRequestUpdates(requestId: String): List<RequestUpdate> {
        return updates[requestId] ?: emptyList()
    }

    override fun registerForUpdates(requestId: String, callback: (RequestUpdate) -> Unit) {
        statusCallbacks.computeIfAbsent(requestId) { mutableListOf() }.add(callback)
    }

    override fun verifyRequestOwnership(requestId: String, customerId: String): Boolean {
        return requests[requestId]?.customerId == customerId
    }

    override fun updateRequest(requestId: String, message: String, newStatus: RequestStatus, updatedBy: String): RequestUpdate {
        val request = requests[requestId] ?: throw IllegalArgumentException("Request not found")
        return addUpdate(requestId, message, newStatus, updatedBy)
    }

    private fun addUpdate(
        requestId: String,
        message: String,
        newStatus: RequestStatus,
        updatedBy: String
    ): RequestUpdate {
        val update = RequestUpdate(
            updateId = "UPD-${UUID.randomUUID()}",
            requestId = requestId,
            status = newStatus,
            message = message,
            timestamp = LocalDateTime.now(),
            updatedBy = updatedBy
        )

        updates[requestId]?.add(update)
        requests[requestId] = requests[requestId]?.copy(status = newStatus) ?: return update

        // Notify callbacks
        statusCallbacks[requestId]?.forEach { callback ->
            callback(update)
        }

        return update
    }
}
