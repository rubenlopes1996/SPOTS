Feature:
  As a registered user
  I want to be able to see statistic
  about duration of 3 algorithms for find me a spot
  in statistics activity

  Scenario: see durations of 3 algorithms
    When I login the app
    And I see the dashboard activity
    And I open the menu
    And I click in Statisctis option
    And I see statistic activity
    Then I see "Distance: 279,27" "Best Rating: 258,20" "My Favorites: 238,40" text