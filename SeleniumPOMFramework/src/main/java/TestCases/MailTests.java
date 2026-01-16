package TestCases;

import org.testng.annotations.Test;

import BaseClasses.PageBaseClass;
import PageClasses.LandingPage;
import PageClasses.LoginPage;
import PageClasses.RediffMailPage;

public class MailTests extends PageBaseClass{

	@Test
	public void writeMailTest(){
		
		LandingPage landingPage = new LandingPage();
		landingPage.openBrowser();
		landingPage.openWebsite();
		landingPage.getTitle();
		
		LoginPage loginPage = landingPage.clickSignIn();
		loginPage.getTitle();
		RediffMailPage rediffmailPage = loginPage.doLogin();
		rediffmailPage.getTitle();
		
	}
}
