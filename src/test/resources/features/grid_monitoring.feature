Feature: Grid Monitoring
  As a grid operator
  I want to monitor grid status
  So that I can ensure reliable power distribution

  Scenario: View grid status
    Given I am a logged-in grid operator
    When I access the grid monitoring dashboard
    Then I should see real-time grid status
    And I should see any active alerts

  Scenario: Handle grid alert
    Given I am a logged-in grid operator
    When a grid alert is triggered
    Then I should receive immediate notification
    And I should see detailed alert information