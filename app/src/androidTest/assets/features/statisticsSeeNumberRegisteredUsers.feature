Feature:
  As a registered user
  I want to be able to see statistic
  about how much users are registered in the app
  in statistics activity

  Scenario: see number of registered users
    When I login the app
    And I see the dashboard activity
    And I open the menu
    And I click in Statisctis option
    And I see statistic activity
    Then I see "4" registered users