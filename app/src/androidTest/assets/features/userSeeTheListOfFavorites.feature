Feature:
  As a registered user
  I want to login and see the dashboard
  After that i click on menu and click Favorites
  I see my favorites activity with all my favorites

  Scenario: User have favorites and see the list
    Given I login
    And I see the dashboard
    And I click in option menu Favorites
    Then I see the My favorites activity

  Scenario: User dont have favorites and see a message
    Given I login
    And I see the dashboard activity
    And I click in option menu Favorites
    Then I see a msg saying "You dont have favorites."