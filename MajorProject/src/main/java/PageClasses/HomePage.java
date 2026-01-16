package PageClasses;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Constants.LocatorsRepository;

public class HomePage extends LocatorsRepository {
	
	public WebDriver driver;
	public WebDriverWait wait;
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
		
	public void hoverOnAccountElement() {
		 Actions actions = new Actions(driver);
			actions.moveToElement(accountElement).build().perform();
			System.out.println("Hovered over Account element.");
			wait.until(ExpectedConditions.elementToBeClickable(signInButton));
			signInButton.click();
			System.out.println("Clicked on Sign In button.");
	}
	
	
	public void enterInvalidNumber() {
		numberInputField.sendKeys("1111111111");
        System.out.println("Entered invalid number in the input field.");
       
	}
	
	
	public void takeScreenshotOfErrorMessage() {
		
		WebElement invalidMobileError = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//*[@class='styles_error__ypKFM']")));
		String errorMessage = invalidMobileError.getText();
		System.out.println("Error Message: " + errorMessage);

		invalidMobileError.getScreenshotAs(OutputType.FILE);
		try {
			
			String filepath = System.getProperty("user.dir") + "/screenshots/" + generateFileName();
			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			FileUtils.copyFile(screenshotFile, new File(filepath));
		
			System.out.println("Screenshot saved as InvalidMobileNumberError.png");
		} catch (IOException e) {
			System.out.println("Failed to save screenshot: " + e.getMessage());
		}
	}
	
	public String generateFileName() {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String date = dateformat.format(new Date());

		String filename = date + ".png";
		System.out.println("Generated screenshot filename: " + filename);
		return filename;
		
	}

		
	public void clearMobileNumber() {
	    wait.until(ExpectedConditions.elementToBeClickable(numberInputField));

	    numberInputField.click();
	    numberInputField.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
	    numberInputField.sendKeys(org.openqa.selenium.Keys.DELETE);

	    System.out.println("Mobile number cleared");
	}

			
	public void enterValidMobileNumber() throws InterruptedException {
			
	    clearMobileNumber();
		numberInputField.sendKeys("8425020564");
		System.out.println("Entered Valid Mobile Number.");
		WebElement getOTPBtn = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains (text(),  'GET OTP')]")));
		getOTPBtn.click();
		System.out.println("Clicked on Get OTP button.");
		
		Thread.sleep(15000);
		
	}
	
	
	 public void clickOnVerifyBtn() {
			WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains (text(), 'VERIFY')]")));
			verifyBtn.click();
			System.out.println("Clicked on Verify button.");
	 }
	 
		public void loginToApplication() throws Exception {
			hoverOnAccountElement();
			Thread.sleep(2000);
			enterInvalidNumber();
			Thread.sleep(2000);
			takeScreenshotOfErrorMessage();
			Thread.sleep(2000);
			generateFileName();
			Thread.sleep(2000);
			enterValidMobileNumber();
			clickOnVerifyBtn();
			System.out.println("Logged in to the application successfully.");
		}
	
				
	public SellCarPage navigateToSellCarPage() {
		return PageFactory.initElements(driver, SellCarPage.class);
	}
		
		 
	  
}
 

