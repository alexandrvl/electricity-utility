Feature: User Authentication
  As a system user
  I want to authenticate myself
  So that I can access the system securely

  Scenario: Successful login
    Given I am a registered user
    When I enter valid credentials
    Then I should be logged into the system
    And I should see my dashboard

  Scenario: Failed login
    Given I am a user
    When I enter invalid credentials
    Then I should see an error message
    And I should remain on the login page