Feature:
  As a user
  I want to type a email and password
  and click on "Login" button to
  access to the dashboard painel

  Background:
    Given I see an empty login form

  Scenario: Cancel Login
    When i click on the cancel button
    Then i see the GuestDashboardActivity

  Scenario Outline: Set of wrong credentials
    When I introduce as username "<user>"
    And I introduce as password "<pass>"
    And I press the login button
    Then I see an error message saying 'Invalid credentials'
    Examples:
      | user            | pass      |
      | admin@gmail.com | admin1    |
      | admin@gmail.com | 123456789 |
      | ruben@gmail.com | asd123    |

  Scenario Outline: Valid username and password
    When I introduce a valid username "<username>"
    And I introduce a valid password "<password>"
    And I press the login button
    Then I see the dashboard screen
    Examples:
      | username        | password  |
      | ruben@hotmail.com | 123456789 |



