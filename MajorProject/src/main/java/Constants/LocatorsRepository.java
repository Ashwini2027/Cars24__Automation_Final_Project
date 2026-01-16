package Constants;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import BaseClasses.BaseClass;

public class LocatorsRepository extends BaseClass {

	
	//****************************HOME PAGE LOCATORS****************************//
	@FindBy(xpath="//*[@class='styles_account__Fd2dS media-body']")
	public WebElement accountElement;
	
	@FindBy(xpath="//button[@class='btn  styles_common-btn__aQ_0w styles_btn-styles__VSAMg    shrinkOnTouch']")
	public WebElement signInButton;
	
	@FindBy(xpath="//input[@name='phone']")
	public WebElement numberInputField;
	
    //****************************SELL CAR PAGE LOCATORS****************************//
	@FindBy(xpath = "//li[@class='styles_sellCarWrapper__RGHIl ']/a")
	public WebElement sellCarMenuOption;

	@FindBy(xpath = "//p[contains (text(), 'Sell car in Mumbai')]")
	public WebElement sellCarInMumbaiText;

	@FindBy(xpath = "//input[@class='form-control']")
	public WebElement inputCarNumber;
	
	@FindBy(xpath = "//button[contains (text(), 'Get instant car price')]")
	public WebElement getCarPriceButton;
	
	//****************************BUY CAR PAGE LOCATORS****************************//
	@FindBy(xpath="//ul[@class='styles_navList__Lu62B']/li[1]")
	public WebElement buyCarMenuOption;
	
	@FindBy(xpath="//button[contains (text(), 'SELECT MANUALLY')]")
	public WebElement selectManuallyButton;
	
//************use below xpath while using select manually option in buy car page****************//
//	@FindBy(xpath="//ul[@class='styles_popularCities__uMXtF']/li[2]")
	@FindBy(xpath="//ul[@class='styles_popularCities__GQ2kU']/li[2]")
	public WebElement selectCityOption;
	
	@FindBy(xpath="//p[normalize-space()='Cars listed by CARS24']")
	public WebElement carsListedByCARS24Text;
	
	@FindBy(xpath="//a[@class='styles_carCardWrapper__sXLIp'][1]")
	public WebElement firstUsedCarCard;
	
    //****************************USED CAR DETAILS PAGE LOCATORS****************************//
	
	@FindBy(xpath=("//div[@class='styles_carName__xzcd4']/h1"))
	public WebElement carTitle;
	
	@FindBy(xpath = "//ul[contains(@class,'styles_content__')]/li")
	protected List<WebElement> carOverviewList;
	
	
	
	
	
	
	
	
}
