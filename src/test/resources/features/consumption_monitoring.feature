Feature: Consumption Monitoring
  As a customer
  I want to monitor my electricity consumption
  So that I can manage my usage effectively

  Scenario: View consumption data
    Given I am a logged-in customer
    When I access my consumption dashboard
    Then I should see my current consumption
    And I should see historical usage patterns

  Scenario: Real-time updates
    Given I am viewing my consumption dashboard
    When new consumption data is available
    Then the dashboard should update automatically
    And show the latest readings