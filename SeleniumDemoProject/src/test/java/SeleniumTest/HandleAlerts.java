package SeleniumTest;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HandleAlerts {
	WebDriver driver = null;

	@Test
	public void handleAlertsTest() throws InterruptedException {
		Assert.assertEquals(driver.getTitle(), "Rediff.com: News | Rediffmail | Stock Quotes | Rediff Gurus");

		driver.findElement(By.linkText("Sign in")).click();
		Assert.assertEquals(driver.getTitle(), "Rediffmail - Free Email for Login with Secure Access");

		driver.findElement(By.id("login1")).sendKeys("shoaibmirajkar80@gmail.com");
		Thread.sleep(3000);

		
		driver.findElement(By.className("signin-btn")).click();
		Thread.sleep(3000);
		
		Alert alert = driver.switchTo().alert();
		Assert.assertEquals(alert.getText(), "Please enter your password");

		alert.accept();

		driver.findElement(By.id("password")).sendKeys("Test@123");
		Thread.sleep(3000);

		// Clear the Text from UserName
		driver.findElement(By.id("login1")).clear();

		driver.findElement(By.className("signin-btn")).click();
		Thread.sleep(3000);

		Alert alert1 = driver.switchTo().alert();
		Assert.assertEquals(alert1.getText(), "Please enter a valid user name");
		alert1.accept();

	}

		
	@BeforeMethod
	public void Setup() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://www.facebook.com/");

	}

	@AfterMethod
	public void finish() {
		driver.quit();
	}
}
