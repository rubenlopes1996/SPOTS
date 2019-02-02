Feature:
  As a guest
  I want to see the park map (Parque A)
  and i can see the spots available information
  then i can see the last updated available information

Scenario Outline:
  When i open the dashboard
  And i see the Parque A map
  And I see the <num_spots> spots available
  And I see the <busy_spots> spots busy
  Then I see the last updated availability
    Examples:
    |num_spots|busy_spots|
    |3|0|