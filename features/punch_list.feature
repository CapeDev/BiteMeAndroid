Feature: Check punch list to see recent records

  Scenario: Search for a restaurant
    Given I see "Welcome to TrakéMoi!"
    When I press "Punch List"
    Then I should see "Punch Punch!"

