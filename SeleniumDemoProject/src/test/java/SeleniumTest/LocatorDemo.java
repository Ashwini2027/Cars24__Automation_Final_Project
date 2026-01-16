package SeleniumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LocatorDemo {

	/*
	 * Test Case -- Verify Rediff SignIn Functionality 
	 * 1. Open Rediff 
	 * 2. Verify
	 * Rediff Page Title 
	 * 3. Click on Sign In 
	 * 4. Verify Web Page Title 
	 * 5. Enter UserName and password 
	 * 6. Click Sign In Button 
	 * 7. Verify Page Title
	 */

	@Test
	public void verifyRediffSignIn() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();

		// 1. Open Rediff
		driver.get("https://www.rediff.com/");

		// 2. Verify Rediff Page Title
		String expectTitle = "Rediff.com: News | Rediffmail | Stock Quotes | Rediff Gurus";
		String actualTitle = driver.getTitle();
		Assert.assertEquals(actualTitle, expectTitle);

		// 3. Click on Sign In
		WebElement signInLink = driver.findElement(By.linkText("Sign in"));
		signInLink.click();

		// 4. Verify Web Page Title
		String expectSinnInTitle = "Rediffmail - Free Email for Login with Secure Access";
		String actualSinnInTitle = driver.getTitle();
		Assert.assertEquals(actualSinnInTitle, expectSinnInTitle);

		// 5. Enter User Name and password
		WebElement userNameTxtBox = driver.findElement(By.id("login1"));
		WebElement passwordTxtBox = driver.findElement(By.id("password"));
		userNameTxtBox.sendKeys("shoaibmirajkar80@gmail.com");
		Thread.sleep(4000);
		passwordTxtBox.sendKeys("Test@1234");
		Thread.sleep(4000);

		// 6. Click Sign In Button
		WebElement signInBtn = driver.findElement(By.name("proceed"));
		signInBtn.click();

		// 7. Verify Page Title
		String expectSinInTitle = "Rediffmail - Free Email for Login with Secure Access";
		String actualSinInTitle = driver.getTitle();
		Assert.assertEquals(actualSinInTitle, expectSinInTitle);
		
		driver.quit();

	}

}

