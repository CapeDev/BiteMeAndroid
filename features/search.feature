@wip
Feature: Search for a restaurant based on food

  Scenario: Search for a restaurant
    Given I see "Welcome to TrakéMoi!"
    And I search for "sushi"
    Then I should see "Akiko's Restaurant"