Feature:
  As a registered user
  I want to be able to edit profile, like name, age
  I want to have a password verification to be able to edit the User
  So that I can click on save button, have my info saved and show me the dashboard activity

  Scenario Outline: Unsuccesfull edit
    When I login the app and see profile activity
    And I introduced password as <password>
    And I introduce name as <name>
    And I introduce age as <age>
    And I see emails as <email>
    And I select preference as <preference>
    And I click the save button 
    Then I see an error message saying <errorMessage>
    Examples:
      |password|name|age|email|preference|errorMessage|
      |111111   |"Ruben Santos"|22|"ruben@hotmail.com"|"Distance"    |"Invalid credentials"                    |
      |123456789|"Ruben Santos"|17|"ruben@hotmail.com"|"My favorites"|"Please introduce a valid age. Age should be between 18-99" |
      |123456789|"Ruben Santos"|100|"ruben@hotmail.com"|"My favorites"|"Please introduce a valid age. Age should be between 18-99"|

  Scenario Outline: Succesfull edit profile
    When I login the app and see profile activity
    And I introduced password as <password>
    And I introduce name as <name>
    And I introduce age as <age>
    And I see emails as <email>
    And I select preference as <preference>
    And I click the save button 
    Then I see the DashboardActivity
    Examples:
      |password|name|age|email|preference|
      |123456789|"Ruben Santos"|22|"ruben@hotmail.com"|"My favorites" |

  Scenario: Cancel edit profile
    When I login the app and see profile activity
    And I click the cancel button 
    Then I see the DashboardActivity