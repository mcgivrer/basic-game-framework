Feature: Rendering pipeline

    This feature provides a full java Graphics2D compatible rendergin pipline.

Scenario: Initialize Render with defautl values
    Given A new App
    When getting the internal render 
    Then the internal rendering buffer is set to 320x200
