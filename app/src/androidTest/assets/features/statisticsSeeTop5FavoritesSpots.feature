Feature:
  As a registered user
  I want to be able to see statistic
  about top 5 spots added to favorites by all users
  in statistics activity

  Scenario: see top 5 favories spots
    When I login the app
    And I see the dashboard activity
    And I open the menu
    And I click in Statisctis option
    And I see statistic activity
    Then I see "Spot8" "Spot2" "Spot4"  "Spot5"  "Spot6" text