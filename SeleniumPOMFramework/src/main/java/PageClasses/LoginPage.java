package PageClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import BaseClasses.PageBaseClass;

public class LoginPage extends PageBaseClass{

	//WebDriver driver;
	
	public LoginPage(WebDriver driver)
	{
		this.driver=driver;
	}
	
	
	public RediffMailPage doLogin()
	{
		//if login successfull
		return PageFactory.initElements(driver,RediffMailPage.class);
		//else
		//return new LoginPage();
	}
	
	public void getTitle()
	{
		return;
	}
	
}
