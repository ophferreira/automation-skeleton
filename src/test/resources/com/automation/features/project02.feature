Feature: TestPlans integration

  @testCaseId:651 @suiteId:641 @azure
  Scenario: Perform a invalid CEP
    Given invalid CEP
    Then will be invalid

  @testCaseId:260 @suiteId:641 @azure
  Scenario: Perform a valid CEP
    Given valid CEP
    Then will be valid