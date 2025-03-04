Feature: Online Payment
  As a customer
  I want to pay my bill online
  So that I can settle my account conveniently

  Scenario: Successful payment
    Given I am viewing my bill
    When I make a payment using valid payment method
    Then the payment should be processed
    And I should receive a receipt

  Scenario: Failed payment
    Given I am viewing my bill
    When I make a payment using invalid payment method
    Then I should see an error message
    And my bill should remain unpaid