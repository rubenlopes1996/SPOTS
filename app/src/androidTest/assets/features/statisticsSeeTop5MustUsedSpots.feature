Feature:
  As a registered user
  I want to be able to see statistic
  about top 5 must used spots
  in statistics activity

  Scenario: see top 5 must used spots
    When I login the app
    And I see the dashboard activity
    And I open the menu
    And I click in Statisctis option
    And I see statistic activity
    Then I see "Spot9" "Spot2" "Spot3"  "Spot1"  "Spot6" text