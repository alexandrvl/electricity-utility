package com.electricity.utility.spec.contracts.steps

import com.electricity.utility.spec.contracts.domain.*
import java.time.LocalDateTime

class DataSecurityStepsImpl(private val dataSecurity: DataSecurity) : DataSecuritySteps {
    private var testData: ByteArray? = null
    private var encryptedData: EncryptedData? = null
    private var testUserId: String? = null
    private var testOperation: String? = null
    private var auditLogEntry: AuditLogEntry? = null

    override fun givenSensitiveDataIsBeingTransmitted() {
        testData = "sensitive test data".toByteArray()
    }

    override fun givenAUserPerformsSensitiveOperations() {
        testUserId = "test-user-123"
        testOperation = "SENSITIVE_OPERATION"
    }

    override fun whenTheDataIsInTransit() {
        testData?.let {
            encryptedData = dataSecurity.encryptData(it, EncryptionMethod.AES_256)
        } ?: throw IllegalStateException("Test data not initialized")
    }

    override fun whenTheOperationIsCompleted() {
        testUserId?.let { userId ->
            testOperation?.let { operation ->
                val details = mapOf(
                    "timestamp" to LocalDateTime.now().toString(),
                    "type" to "TEST_OPERATION",
                    "status" to "COMPLETED"
                )
                auditLogEntry = dataSecurity.logOperation(userId, operation, details)
            } ?: throw IllegalStateException("Test operation not initialized")
        } ?: throw IllegalStateException("Test user not initialized")
    }

    override fun thenItShouldBeEncrypted() {
        encryptedData?.let {
            if (!dataSecurity.isEncrypted(it)) {
                throw AssertionError("Data is not properly encrypted")
            }
        } ?: throw IllegalStateException("Encrypted data not initialized")
    }

    override fun thenSecureFromUnauthorizedAccess() {
        encryptedData?.let {
            if (!dataSecurity.isSecure(it)) {
                throw AssertionError("Data is not secure from unauthorized access")
            }
        } ?: throw IllegalStateException("Encrypted data not initialized")
    }

    override fun thenItShouldBeLoggedInAuditTrail() {
        testUserId?.let { userId ->
            val auditTrail = dataSecurity.getUserAuditTrail(userId)
            if (auditTrail.isEmpty()) {
                throw AssertionError("No audit trail found for user")
            }
            if (auditLogEntry !in auditTrail) {
                throw AssertionError("Operation not found in audit trail")
            }
        } ?: throw IllegalStateException("Test user not initialized")
    }

    override fun thenIncludeRelevantOperationDetails() {
        auditLogEntry?.let { entry ->
            if (entry.userId != testUserId) {
                throw AssertionError("Incorrect user ID in audit log")
            }
            if (entry.operation != testOperation) {
                throw AssertionError("Incorrect operation in audit log")
            }
            if (entry.details.isEmpty()) {
                throw AssertionError("No operation details in audit log")
            }
            if (entry.status != OperationStatus.COMPLETED) {
                throw AssertionError("Incorrect operation status in audit log")
            }
        } ?: throw IllegalStateException("Audit log entry not initialized")
    }
}
