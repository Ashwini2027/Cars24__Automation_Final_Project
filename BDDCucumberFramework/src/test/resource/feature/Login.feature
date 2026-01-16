Feature: Login Application feature


Scenario: User Login Scenario
	Given User is on Application Home Page 
	When Application Page Title is Facebook
	Then User enters username and password
	And User clicks on Login button
	Then User navigates to User Profile page
