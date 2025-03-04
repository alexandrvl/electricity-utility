Feature: Usage Analytics
  As a system analyst
  I want to analyze consumption patterns
  So that I can identify trends and anomalies

  Scenario: Generate usage report
    Given I am a system analyst
    When I request a usage analysis report
    Then the system should generate the report
    And show consumption patterns and trends

  Scenario: Detect anomalies
    Given I am monitoring usage patterns
    When an unusual consumption pattern occurs
    Then the system should flag the anomaly
    And generate an alert for investigation