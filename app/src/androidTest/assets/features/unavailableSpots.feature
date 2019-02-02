Feature:
  As a User
  The user sees the dashboard activity and check the number of unavailable spots

  Scenario Outline:
    When i login the app
    And I click the spinner
    And I select the park <park> map
    And i see the park <park> map
    Then I see the <num_spots> spots unavailable
    Examples:
      |park|num_spots|
      |"Parque A"|0|
      |"Parque B"|0|
      |"Parque C"|1|