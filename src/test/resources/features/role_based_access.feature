Feature: Role-based Access
  As a system administrator
  I want to manage user roles
  So that users can access appropriate features

  Scenario: Assign user role
    Given I am a system administrator
    When I assign a role to a user
    Then the user should have appropriate permissions
    And access to relevant features

  Scenario: Restrict access
    Given I am a user with limited permissions
    When I attempt to access restricted features
    Then I should be denied access
    And see an appropriate message