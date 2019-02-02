Feature:
  As a registered user
  I want to be able to tell the app
  that i leave the spot where I'm parked

  Scenario: User leave a spot and notify the app
    When I see the login form and logged in
    And I open the dashboard
    And I click in the button my spot
    And I click yes in the confirmation dialog that I leave the spot
    Then The number of free spots change

  Scenario: User try to leave a spot but dont have a currentSpot
    When I see the login form and logged in
    And I open the dashboard
    And I click in the button my spot to leave the spot
    Then I see a message to take the spot