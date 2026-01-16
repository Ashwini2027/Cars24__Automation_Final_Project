package SeleniumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExtractTextFromWebElement {

	public WebDriver driver = null;
	
	@BeforeMethod
	public void Setup() {
		System.setProperty("webdriver.chrome.driver",
				"D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\chromedriver.exe");

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://www.facebook.com/");

	}

	@AfterMethod
	public void finish() {
		driver.quit();
	}
	
	
	@Test
	public void extractTextFromWebpage() {
		//Extracting Text from heading
		WebElement heading= driver.findElement(By.className("_8eso"));
		Assert.assertEquals(heading.getText(), "Facebook helps you connect and share with the people in your life.");
	}
	
	
}
