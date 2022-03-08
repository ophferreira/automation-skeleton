Feature: Scenario Outline Substitution

  Scenario Outline: Email confirmation
    Given I have a user account with my name "<Name>"
    When an Admin grants me <Role> rights
    Then I should receive an email with the body:
    """
    Dear <Name>,
    You have been granted <Role> rights.  You are <details>. Please be responsible.
    -The Admins
    """
    Examples:
      | Name                | Role              | details         |
      | Thomaz Zambotti     | Manager | now able to manage your employee accounts     |
      | Jean Finck | Admin  | able to manage any user account on the system |