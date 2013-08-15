Feature: Check home page with essential element

  Scenario: launch the app
    Given I see "TrakéMoi"
    When I press "Punch List"
    Then I should see nothing
