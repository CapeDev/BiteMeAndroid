@wip
Feature: Login feature

  Scenario: Navigate to Location page
    Given I see "Welcome to TrakéMoi!"
    When I press "Location"
    Then I swipe left
    Then I swipe right

    When I go back
    Then I see "Welcome to TrakéMoi!"
