Feature: Check punch list to see recent records

  Scenario: Punch in
    Given I see "Punch List"
    When I press "Punch List"
    Then I should see "Punch Punch!"
    And I press "In"
    Then I should see "Out"

  Scenario: Punch out
    Given I see "Punch List"
    When I press "Punch List"
    Then I should see "Out"
    And I press "Out"
    Then I should see "In"