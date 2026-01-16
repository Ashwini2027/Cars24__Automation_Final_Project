package PageClasses;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Constants.LocatorsRepository;

public class SellCarPage extends LocatorsRepository {
	public WebDriver driver;
	public WebDriverWait wait;
	
	public SellCarPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
	
	public void clickOnSellCarMenuOption() {
		String homePageID = driver.getWindowHandle();
		System.out.println("Main Page Window ID : " + homePageID);
		Actions actions = new Actions(driver);
		actions.moveToElement(sellCarMenuOption).build().perform();
		waitForElement(sellCarInMumbaiText).click();
		System.out.println("Clicked on Sell Car Menu Option.");
	}
	
	public void windowHandleForSellCarPage() {
	Set<String> scPageID = driver.getWindowHandles();
	Iterator<String> itr = scPageID.iterator();
	String scPageID1 = itr.next();
	System.out.println("Sell Car Page Window ID : " + scPageID);
		for (String windowId : driver.getWindowHandles()) {
			if (!windowId.equals(scPageID1)) {
				driver.switchTo().window(windowId);
				break;
			}
		}
		System.out.println("Switched to Sell Car Page window.");
	}
	
	
	public void enterCarNumber() {
		Actions actions = new Actions(driver);
		actions.click(inputCarNumber);

		for (char c : "MH04FA5650".toCharArray()) {
		    actions.sendKeys(String.valueOf(c)).pause(Duration.ofMillis(400));
		}
		actions.build().perform();
		System.out.println("Entered Car Number in Sell Car Page.");
	}

	

	public void clickOnGetCarPriceButton() {
		getCarPriceButton.click();
		System.out.println("Clicked on Get Car Price button.");
	}
	
	
	public void scrollDownSCPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,300)");
		System.out.println("Scrolled down the Sell Car Page.");
	}
	
	public void clickOnConfirmBtn() {
		WebElement confirmButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains (text(), 'Confirm')]")));
		confirmButton.click();
		System.out.println("Clicked on Confirm button.");
	}

	public void selectKilometersDriven() {
		WebElement kmDrivenOption = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='HYJK6']/ul/li[10]")));
		kmDrivenOption.click();
		System.out.println("Selected Kilometers Driven option.");
	}

	public void selectCarLocation() {
        WebElement carLocationOption = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='_17bAX']/a/span/img")));
        carLocationOption.click();
        System.out.println("Selected Car Location option.");
      
	}
	
	public void sellingDaysOption() {
		WebElement sellingDaysOption = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='_1iUDj']/li[4]")));
		sellingDaysOption.click();
		System.out.println("Selected Selling Days option.");
	}
	
	
	public void switchToMainWindow() {
		driver.close();
		String mainWindowHandle = driver.getWindowHandles().iterator().next();
		driver.switchTo().window(mainWindowHandle);
		System.out.println("Switched back to Main Window.");
	}
	
	
	public void sellCarProcess() throws InterruptedException {
		Thread.sleep(2000);
		clickOnSellCarMenuOption();
		Thread.sleep(2000);
		windowHandleForSellCarPage();
		enterCarNumber();
		Thread.sleep(2000);
		clickOnGetCarPriceButton();
		Thread.sleep(2000);
		scrollDownSCPage();
		Thread.sleep(2000);
		clickOnConfirmBtn();
		Thread.sleep(4000);
		selectKilometersDriven();
		Thread.sleep(4000);
		selectCarLocation();
		Thread.sleep(4000);				
		sellingDaysOption();
		Thread.sleep(4000);
		switchToMainWindow();
	}
	
	
		public BuyCarPage navigateToBuyCarPage() {
		return PageFactory.initElements(driver, BuyCarPage.class);
	}
	
}

