Feature:
  As a registered user
  I want to be able to tell the app
  that i leave the spot where I'm parked
  and rate that spot

  Scenario: User leave a spot and rate the spot
    When I see the login form and logged in
    And I open the dashboard
    And I click in the button my spot
    And I click yes in the confirmation dialog that I leave the spot
    And I rate the spot
    And I click ok
    Then The number of free spots change

  Scenario: User leave a spot and dont rate the spot
    When I see the login form and logged in
    And I open the dashboard
    And I click in the button my spot
    And I click yes in the confirmation dialog that I leave the spot
    And I click ok
    Then The number of free spots change