package PageClasses;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import Constants.LocatorsRepository;

public class BuyCarPage extends LocatorsRepository {
	public WebDriver driver;
	public WebDriverWait wait;
	
	public BuyCarPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
		
	public void clickOnBuyCarMenuOption() {
		buyCarMenuOption.click();
		System.out.println("Clicked on Buy Car menu option.");
        Actions actions = new Actions(driver);
		actions.moveByOffset(0, 0).perform();
	}
	
		
	public void clickOnSelectManually() {
		selectManuallyButton.click();
	}
		
	
	public void selectCity() {
		selectCityOption.click();
		System.out.println("Selected City option.");
	}
	
	
	public void selectCarsListedByCARS24Text() {
		carsListedByCARS24Text.isDisplayed();
		Actions actions = new Actions(driver);
		actions.moveToElement(carsListedByCARS24Text).click().build().perform();
		System.out.println("Cars listed by CARS24 text is selected.");
	}
	
	public void scrollDownBCPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,300)");
		System.out.println("Scrolled down on Buy Car Page.");
	}
	
	
	public void firstUsedCarCard() {
		firstUsedCarCard.click();
        System.out.println("Clicked on 1st Used Car Card.");
	}
	
	public void windowHandleForCarDetailsPage() {

	    String parentWindow = driver.getWindowHandle();
	    Set<String> allWindows = driver.getWindowHandles();

	    System.out.println("All Window IDs: " + allWindows);

	    for (String windowId : allWindows) {
	        if (!windowId.equals(parentWindow)) {
	            driver.switchTo().window(windowId);
	            System.out.println("Switched to Car Details Page window.");
	            break;
	        }
	    }
	}


	
	public void buyCarProcess() throws Exception {
		Thread.sleep(2000);
		clickOnBuyCarMenuOption();
		Thread.sleep(2000);
//		clickOnSelectManually();
		Thread.sleep(2000);
		selectCity();
		Thread.sleep(2000);
		selectCarsListedByCARS24Text();
		Thread.sleep(2000);
		scrollDownBCPage();
		Thread.sleep(4000);
		firstUsedCarCard();
		Thread.sleep(2000);
		windowHandleForCarDetailsPage();
	}
	
		
		public UsedCarDetailsPage navigateToUsedCarDetailsPage() {
			
			return PageFactory.initElements(driver, UsedCarDetailsPage.class);
		}
	
	
}
