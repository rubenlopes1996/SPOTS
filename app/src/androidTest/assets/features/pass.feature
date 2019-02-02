Feature:
  As a registered user
  I want to be able to change password
  I want to insert my old password and set a new one
  So that I can click on save button, have my info saved and show me the dashboard activity

  Scenario Outline: Unsuccesfull edit
    When I login the app and see password activity
    And I introduced old password as <password>
    And I introduced new password as <newPassword>
    And I click the save button 
    Then I see an error message saying <errorMessage>
    Examples:
      |password|newPassword|errorMessage|
      |111111   |123456  |"Invalid credentials"|

  Scenario Outline: Succesfull edit
    When I login the app and see password activity
    And I introduced old password as <password>
    And I introduced new password as <newPassword>
    And I click the save button 
    Then I see the DashboardActivity
    Examples:
      |password|newPassword|
      |12345678|12345678  |

  Scenario: Succesfull edit
    When I login the app and see password activity
    And I click the cancel button 
    Then I see the DashboardActivity