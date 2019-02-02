Feature:
  As a registered user
  I want to see the Dashboard activity
  After that I will see my current Location
  That I receive a notification in the nearest spot avaliable
  then I can accept, and I get the requested spot or cancel the notification.

  Scenario: User can accept the automatic sugestion where the car is parked
    When I see the Dashboard activity
    And Allow permission to get my location
    And I receive a notification in the nearest spot avaliable
    Then I can accept, and I get the requested spot.
