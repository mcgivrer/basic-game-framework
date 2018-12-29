Feature: Application has Command line Features
    Some basic verification around executing the App class instance from command line.

Scenario: Application has a title
Given An application with title "Hello"
When getting the application title
Then title is "Hello".

Scenario: Application has at least one GameObject
Given An application wihtout arg
When getting the object list
Then it has at least 1 GameObject

Scenario: Application has one window
Given An Application with args 
And has arg w=320 
And has arg h=240
When getting the buffer
Then the buffer is not null and has size (320x240)

Scenario: Application has debug mode
Given An Application with args
And has arg d=2
When getting debug mode
Then the debug mode is 2

Scenario: Application has a scale mode
Given An Application with args
And has arg s=2.0
When getting scale value
Then the scale value is 2.0

Scenario: Application has an audio mode
Given An Application with args
And has arg a=0
And Application is initialized
When getting audio mode
Then the audio mode is 1

Scenario: Application has a full screen mode
Given An Application fullWindow
And has arg f=1
And Application is initialized
When getting fullscreen status
Then the fullscreen status is 1
Then the window is in full screen mode
