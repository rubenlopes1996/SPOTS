Feature:
  As a user
  I want to be able to click on username in the main window, and then have access to User logout.
  Within the Logout menu window, I want to be able to choose if I want to leave the app or continue logged in.

  Scenario: Logoff
    When i login to the app
    And I click on the menu dropdown
    And I click on the Logoff button
    Then I see the Login Activity

