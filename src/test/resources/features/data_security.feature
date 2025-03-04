Feature: Data Security
  As a system operator
  I want to ensure data protection
  So that sensitive information remains secure

  Scenario: Encrypt sensitive data
    Given sensitive data is being transmitted
    When the data is in transit
    Then it should be encrypted
    And secure from unauthorized access

  Scenario: Audit trail
    Given a user performs sensitive operations
    When the operation is completed
    Then it should be logged in audit trail
    And include relevant operation details