@wip
Feature: Login feature

  Scenario: Navigate to Location page
    Given I see "Hello bitemoi!"
    When I press "Location"
    Then I swipe left
    Then I swipe right

    When I go back
    Then I see "Hello bitemoi!"
