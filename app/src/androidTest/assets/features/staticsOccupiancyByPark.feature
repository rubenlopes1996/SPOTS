Feature:
  As a registered user
  I want to be able to see statistic
  about the occupiancy of a park
  in statistics activity


  Scenario: See the occupiancy rate per park
    When i login to the app
    And I see the dashboard activity
    And I open the menu
    And I click in Statisctis option
    And I see statistic activity
    Then I see percentage of occupiancy by park