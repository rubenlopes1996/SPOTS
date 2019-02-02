Feature:
  As a user
  i want to select the park map
  and i want to see the park map selected (Parque A, Parque B, Parque C)
  then i can see the spots available information

  Scenario Outline:
    When i login the app
    And I click the spinner
    And I select the park <park> map
    And i see the park <park> map
    Then I see the <num_spots> spots available
      Examples:
      |park|num_spots|
      |"Parque A"|2|
      |"Parque B"|1|
      |"Parque C"|0|