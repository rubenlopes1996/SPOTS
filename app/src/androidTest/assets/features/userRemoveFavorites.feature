Feature:
  As a registered user
  I want to login and see the dashboard
  In the menu I click Favorites
  I see my favorites activity and I can remove favorites

  Scenario: I see favorites and i can remove favorites
    When I login the app
    And I see the dashboard activity
    And I open the menu
    And I click in Favorites option
    And I see Favorites activity
    Then I choose a favorite and I remove it.

  Scenario: I don't see favorites, so I can't remove
    When I login the app
    And I see the dashboard activity
    And I open the menu
    And I click in Favorites option
    And I see Favorites activity
    Then I can't remove favorites because there isn't any