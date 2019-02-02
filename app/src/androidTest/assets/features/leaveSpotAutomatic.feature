Feature:
  As a user I want to accept or
  decline the suggestion to leave
  the spot

  Scenario: User can accept the automatic request to leave the spot
    When I see the Dashboard activity
    And I receive a notification to leave the spot
    Then I can accept, and I get the requested spot.

  Scenario: User can decline the automatic request to leave the spot
    When I see the Dashboard activity
    And I receive a notification to leave the spot
    Then I can cancel, and I stay in the same spott