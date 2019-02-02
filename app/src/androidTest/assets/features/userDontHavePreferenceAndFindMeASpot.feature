Feature:
  As a registered user
  I want to login and see the dashboard
  After that i click on find me a spot button
  the app see that i dont have preference and ask me to change my preference
  After that i click on find me a spot
  the app give me a spot to park

  Scenario: User dont have preference and click find me
    Given I open the dashboard
    And I click on the button find me a spot without preference
    And I receive a notification to change the preferences
    And I click yes to accept
    And I see profile activity
    And I change the preference to "Distance"
    And I go to dashboard again
    And I click on find me a spot button
    And I receive a notification to the "Distance" to the "Spot1"
    And I click yes to accept the spot
    And I receive a notification that i have parked in the "Spot1"
    And I Click yes to accept the automatic notification to park
    Then the number of free spots change