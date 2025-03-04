package com.electricity.utility.spec.contracts.steps

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import com.electricity.utility.spec.contracts.domain.*
import java.time.LocalDateTime

class DataSecurityStepsImplTest {
    private val dataSecurity = mockk<DataSecurity>(relaxed = true)
    private lateinit var dataSecuritySteps: DataSecurityStepsImpl

    @BeforeEach
    fun setup() {
        clearAllMocks()
        dataSecuritySteps = DataSecurityStepsImpl(dataSecurity)
    }

    @Test
    fun `test successful data encryption scenario`() {
        // Given
        val testData = "sensitive test data".toByteArray()
        val encryptedData = EncryptedData(
            dataId = "test-data-1",
            content = testData,
            encryptionMethod = EncryptionMethod.AES_256,
            timestamp = LocalDateTime.now(),
            metadata = mapOf("type" to "test")
        )

        every { 
            dataSecurity.encryptData(any(), EncryptionMethod.AES_256) 
        } returns encryptedData
        every { 
            dataSecurity.isEncrypted(encryptedData) 
        } returns true
        every { 
            dataSecurity.isSecure(encryptedData) 
        } returns true

        // When & Then
        dataSecuritySteps.givenSensitiveDataIsBeingTransmitted()
        dataSecuritySteps.whenTheDataIsInTransit()
        dataSecuritySteps.thenItShouldBeEncrypted()
        dataSecuritySteps.thenSecureFromUnauthorizedAccess()

        verify(exactly = 1) { dataSecurity.encryptData(testData, EncryptionMethod.AES_256) }
        verify(exactly = 1) { dataSecurity.isEncrypted(encryptedData) }
        verify(exactly = 1) { dataSecurity.isSecure(encryptedData) }
    }

    @Test
    fun `test successful audit trail scenario`() {
        // Given
        val userId = "test-user-123"
        val operation = "SENSITIVE_OPERATION"
        val auditLogEntry = AuditLogEntry(
            entryId = "audit-1",
            userId = userId,
            operation = operation,
            timestamp = LocalDateTime.now(),
            details = mapOf(
                "timestamp" to LocalDateTime.now().toString(),
                "type" to "TEST_OPERATION",
                "status" to "COMPLETED"
            ),
            status = OperationStatus.COMPLETED
        )

        every { 
            dataSecurity.logOperation(
                userId,
                operation,
                any()
            )
        } returns auditLogEntry

        every { 
            dataSecurity.getUserAuditTrail(userId) 
        } returns listOf(auditLogEntry)

        // When & Then
        dataSecuritySteps.givenAUserPerformsSensitiveOperations()
        dataSecuritySteps.whenTheOperationIsCompleted()
        dataSecuritySteps.thenItShouldBeLoggedInAuditTrail()
        dataSecuritySteps.thenIncludeRelevantOperationDetails()

        verify(exactly = 1) { dataSecurity.logOperation(userId, operation, any()) }
        verify(exactly = 1) { dataSecurity.getUserAuditTrail(userId) }
    }

    @Test
    fun `test encryption failure scenario`() {
        // Given
        val testData = "sensitive test data".toByteArray()
        val encryptedData = EncryptedData(
            dataId = "test-data-1",
            content = testData,
            encryptionMethod = EncryptionMethod.AES_256,
            timestamp = LocalDateTime.now(),
            metadata = mapOf("type" to "test")
        )

        every { 
            dataSecurity.encryptData(any(), EncryptionMethod.AES_256) 
        } returns encryptedData
        every { 
            dataSecurity.isEncrypted(encryptedData) 
        } returns false

        // When & Then
        dataSecuritySteps.givenSensitiveDataIsBeingTransmitted()
        dataSecuritySteps.whenTheDataIsInTransit()

        assertThrows<AssertionError> {
            dataSecuritySteps.thenItShouldBeEncrypted()
        }
    }

    @Test
    fun `test security verification failure scenario`() {
        // Given
        val testData = "sensitive test data".toByteArray()
        val encryptedData = EncryptedData(
            dataId = "test-data-1",
            content = testData,
            encryptionMethod = EncryptionMethod.AES_256,
            timestamp = LocalDateTime.now(),
            metadata = mapOf("type" to "test")
        )

        every { 
            dataSecurity.encryptData(any(), EncryptionMethod.AES_256) 
        } returns encryptedData
        every { 
            dataSecurity.isEncrypted(encryptedData) 
        } returns true
        every { 
            dataSecurity.isSecure(encryptedData) 
        } returns false

        // When & Then
        dataSecuritySteps.givenSensitiveDataIsBeingTransmitted()
        dataSecuritySteps.whenTheDataIsInTransit()
        dataSecuritySteps.thenItShouldBeEncrypted()

        assertThrows<AssertionError> {
            dataSecuritySteps.thenSecureFromUnauthorizedAccess()
        }
    }

    @Test
    fun `test audit trail verification failure scenario`() {
        // Given
        val userId = "test-user-123"
        val operation = "SENSITIVE_OPERATION"
        val auditLogEntry = AuditLogEntry(
            entryId = "audit-1",
            userId = userId,
            operation = operation,
            timestamp = LocalDateTime.now(),
            details = mapOf(),  // Empty details to trigger verification failure
            status = OperationStatus.COMPLETED
        )

        every { 
            dataSecurity.logOperation(
                userId,
                operation,
                any()
            )
        } returns auditLogEntry

        every { 
            dataSecurity.getUserAuditTrail(userId) 
        } returns listOf(auditLogEntry)

        // When & Then
        dataSecuritySteps.givenAUserPerformsSensitiveOperations()
        dataSecuritySteps.whenTheOperationIsCompleted()
        dataSecuritySteps.thenItShouldBeLoggedInAuditTrail()

        assertThrows<AssertionError> {
            dataSecuritySteps.thenIncludeRelevantOperationDetails()
        }
    }
}
