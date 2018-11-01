Feature: Rendering Objects list

    This feature provides a rendering list for GameObject's

Scenario: Add a GameObject to rendering pipe
Given A new App with viewport set to 320 x 240
When adding 1 GameObject
Then the internal rendering list contains 1 element

Scenario: Add a list of 3 GameObject to rendering pipe
Given A new App with viewport set to 320 x 240
When adding a list of 3 GameObject
Then the internal rendering list contains 3 element

