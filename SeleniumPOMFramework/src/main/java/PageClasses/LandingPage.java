package PageClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import BaseClasses.PageBaseClass;

public class LandingPage extends PageBaseClass{
	


	public LandingPage(WebDriver driver)
	{
		this.driver =driver;
	
	}
	// All elements of Landing Page and associated Operations
		//WebDriver driver;
		
		public void openBrowser()
		{
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
	
			driver = new ChromeDriver();
	
			
		}
		
		
		public void openWebsite()
		{	
			driver.get("https://www.rediff.com");
		}
		
		
		@FindBy(xpath="//*[contains(@class,'toplinks')]/a[2]")
		public WebElement moneyLink;
		
		
		public MoneyPage clickMoneyLink()
		{
			moneyLink.click();
			return PageFactory.initElements(driver,MoneyPage.class);
		}
		
		
		public LoginPage clickSignIn()
		{
			//Perform click
			
			return PageFactory.initElements(driver,LoginPage.class);
		}
		
		
		public void getTitle()
		{
			
		}
	//	
		////Open Browser Code
		//Webelements
		//click
		
	}


