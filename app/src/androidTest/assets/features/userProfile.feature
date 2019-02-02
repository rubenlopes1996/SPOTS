Feature:
  As a registered user
  I want to be able to click on my username in the Dashboard Activity, and then have access to the Profile Acitivity
  To have access to all the fields associated with the User.
  Inside the Profile Acitivity, I want to be able to edit my profile.

  Scenario: User see the profile
    When I open profile
    And I click on the Profile button
    And I see my name
    And I see my age
    And I see my email
    Then I see the Save button