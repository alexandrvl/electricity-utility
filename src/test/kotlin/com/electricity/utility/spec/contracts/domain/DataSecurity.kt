package com.electricity.utility.spec.contracts.domain

import java.time.LocalDateTime

/**
 * Represents encrypted data in transit
 */
data class EncryptedData(
    val dataId: String,
    val content: ByteArray,
    val encryptionMethod: EncryptionMethod,
    val timestamp: LocalDateTime,
    val metadata: Map<String, String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EncryptedData
        return dataId == other.dataId &&
                content.contentEquals(other.content) &&
                encryptionMethod == other.encryptionMethod &&
                timestamp == other.timestamp &&
                metadata == other.metadata
    }

    override fun hashCode(): Int {
        var result = dataId.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + encryptionMethod.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + metadata.hashCode()
        return result
    }
}

/**
 * Represents an audit log entry
 */
data class AuditLogEntry(
    val entryId: String,
    val userId: String,
    val operation: String,
    val timestamp: LocalDateTime,
    val details: Map<String, String>,
    val status: OperationStatus
)

/**
 * Defines encryption methods
 */
enum class EncryptionMethod {
    AES_256,
    RSA_2048,
    TRIPLE_DES,
    BLOWFISH
}

/**
 * Defines operation statuses for audit
 */
enum class OperationStatus {
    INITIATED,
    COMPLETED,
    FAILED,
    DENIED
}

/**
 * Contract for data security operations
 */
interface DataSecurity {
    /**
     * Encrypts sensitive data for transmission
     * @param data The data to encrypt
     * @param method The encryption method to use
     * @return The encrypted data
     */
    fun encryptData(data: ByteArray, method: EncryptionMethod): EncryptedData

    /**
     * Verifies data encryption
     * @param data The data to verify
     * @return true if data is properly encrypted, false otherwise
     */
    fun isEncrypted(data: EncryptedData): Boolean

    /**
     * Checks if data is secure from unauthorized access
     * @param data The data to check
     * @return true if data is secure, false otherwise
     */
    fun isSecure(data: EncryptedData): Boolean

    /**
     * Logs an operation in the audit trail
     * @param userId The ID of the user performing the operation
     * @param operation The operation being performed
     * @param details The operation details
     * @return The created audit log entry
     */
    fun logOperation(userId: String, operation: String, details: Map<String, String>): AuditLogEntry

    /**
     * Gets audit trail for a user
     * @param userId The ID of the user
     * @return List of audit log entries for the user
     */
    fun getUserAuditTrail(userId: String): List<AuditLogEntry>

    /**
     * Gets audit trail for an operation type
     * @param operation The operation type
     * @return List of audit log entries for the operation
     */
    fun getOperationAuditTrail(operation: String): List<AuditLogEntry>

    /**
     * Registers a callback for security events
     * @param callback The function to handle security events
     */
    fun registerSecurityEventCallback(callback: (AuditLogEntry) -> Unit)
}
