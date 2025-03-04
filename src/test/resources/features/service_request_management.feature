Feature: Service Request Management
  As a customer
  I want to submit service requests
  So that I can get support when needed

  Scenario: Submit service request
    Given I am a logged-in customer
    When I submit a service request
    Then the request should be recorded
    And I should receive a confirmation

  Scenario: Track request status
    Given I have submitted a service request
    When I check the request status
    Then I should see the current status
    And any updates to my request