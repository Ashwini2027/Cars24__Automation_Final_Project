package TestCases;

import org.testng.annotations.Test;

import BaseClasses.PageBaseClass;
import PageClasses.LandingPage;
import PageClasses.MoneyPage;
import PageClasses.MyPortfolioPage;
import PageClasses.PortfolioLoginPage;

public class MyPortfolioTest extends PageBaseClass {

	LandingPage landingPage;
	MoneyPage moneyPage;
	PortfolioLoginPage portfoliologinPage;
	MyPortfolioPage myportfolioPage;
	
	
	
	@Test
	public void openPortfolio()
	{
	PageBaseClass pageBase = new PageBaseClass();
	pageBase.invokeBrowser("Chrome");
	landingPage=pageBase.OpenApplication();
	
	moneyPage = landingPage.clickMoneyLink();
	portfoliologinPage= moneyPage.clickSignInLink();
	myportfolioPage=portfoliologinPage.doLogin("shoaibmirajkar80@gmail.com", "Test");
	myportfolioPage.getTitle("Rediff MyPortfolio");
	}

}


