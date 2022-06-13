Feature: Test consult the via CEP API - project01

  @viaCep @project01
  Scenario: Test Get viaCEP - project01
    Given User calls "viaCEP" with GET http request
    Then Status code is 200
    And Validate the schema "viaCEP" and status code is 200
    And "ddd" in response body is "12"

  @viaCep @project01
  Scenario: Test Get viaCEP 1
    Given User calls "viaCEP2" with GET http request
    Then Status code is 200
    And Validate the schema "viaCEP1" and status code is 200
    And "ddd" in response body is "12"

  @viaCep @project01
  Scenario Outline: Test Get viaCEP 1
    Given an address code <cep>
    When User calls "viaCEP1" with "GET" http request
    Then Status code is 200
    And Validate the schema "viaCEP" and status code is 200
    Examples:
      | cep        |
      | "12248310" |
      | "12248330" |
      | "12248320" |

