Feature: Bill Generation
  As a billing administrator
  I want to generate customer bills
  So that customers can be charged correctly

  Scenario: Generate monthly bill
    Given I am a billing administrator
    When I initiate monthly billing
    Then bills should be generated for all active accounts
    And should include all consumption data

  Scenario: View bill details
    Given I am a logged-in customer
    When I access my billing section
    Then I should see my current bill
    And I should see payment history