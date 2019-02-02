Feature:
  As a user
  I want to select a park
  then i can see the park map

  Background:
    Given I login

  Scenario Outline: I see the spinner as a User
    Given I open the dashboard
    And I click the spinner
    And i select the park  <park>
    Then I see the <park> map
    Examples:
    |park|
    |"Parque A"|
    |"Parque B"|
    |"Parque C"|
