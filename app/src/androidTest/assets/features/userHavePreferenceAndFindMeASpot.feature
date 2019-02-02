Feature:
  As a registered user
  I want to login and see the dashboard
  After that i click on find me a spot button
  the app see my preference and give me a spot to park

  Background:
    Given I login

  Scenario Outline: User have preference to best distance
    Given I open the dashboard
    And I click on the button find me a spot with <preference>
    And I receive a notification to the <preference> to the <spot>
    And I click yes to accept the spot
    And I receive a notification that i have parked in the <spot>
    And I Click yes to accept the automatic notification to park
    Then The number of free spots change
    Examples:
    | preference   | spot    |
    |"Best rating" | "Spot2" |

  Scenario Outline: User have preference to best rating
    Given I open the dashboard
    And I click on the button find me a spot with <preference>
    And I receive a notification to the <preference> to the <spot>
    And I click yes to accept the spot
    And I receive a notification that i have parked in the <spot>
    And I Click yes to accept the automatic notification to park
    Then The number of free spots change
    Examples:
      | preference   | spot    |
      |"Distance"    | "Spot1" |

  Scenario Outline: User have preference to my favorites
    Given I open the dashboard
    And I click on the button find me a spot with <preference>
    And I receive a notification to the <preference> to the <spot>
    And I click yes to accept the spot
    And I receive a notification that i have parked in the <spot>
    And I Click yes to accept the automatic notification to park
    Then The number of free spots change
    Examples:
      | preference     | spot    |
      |"My favorites" | "Spot3" |
