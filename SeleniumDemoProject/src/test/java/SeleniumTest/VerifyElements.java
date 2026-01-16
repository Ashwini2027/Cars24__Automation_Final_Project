package SeleniumTest;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class VerifyElements {
	//@Test
		public void verifySelectedTest() throws InterruptedException {
			Assert.assertEquals(driver.getTitle(), "jQuery UI");

			// Click on Check Radio Button
			driver.findElement(By.linkText("Checkboxradio")).click();
			Assert.assertEquals(driver.getTitle(), "Checkboxradio | jQuery UI");

			driver.switchTo().frame(driver.findElement(By.className("demo-frame")));

			// Verify the Radio Button and CheckBox are Selected
			List<WebElement> checkBoxes = driver
					.findElements(By.xpath("//*[@class='ui-checkboxradio-icon ui-corner-all ui-icon ui-icon-background ui-icon-blank']"));

			for (WebElement checkBox : checkBoxes) {
				Assert.assertFalse(checkBox.isSelected());
				System.out.println(checkBox.getText() + "-- Status :: " + checkBox.isSelected());
			}

			System.out.println("**********************");

			for (WebElement checkBox : checkBoxes) {
				checkBox.click();
			}
			
			for (WebElement checkBox : checkBoxes) {
				//Assert.assertTrue(checkBox.isSelected());
				System.out.println(checkBox.getText() + "-- Status :: " + checkBox.isSelected());
			}

		}

		@Test
		public void verifyElementEnableTest() {
			Assert.assertEquals(driver.getTitle(), "jQuery UI");

			// Click on Spinner
			driver.findElement(By.linkText("Spinner")).click();
			Assert.assertEquals(driver.getTitle(), "Spinner | jQuery UI");

			driver.switchTo().frame(driver.findElement(By.className("demo-frame")));

			// Verify Element Enable and Disable
			WebElement targetElemt = driver.findElement(By.id("spinner"));
			WebElement disableBtn = driver.findElement(By.id("disable"));

			Assert.assertTrue(targetElemt.isEnabled());
			System.out.println("Initial Element Status :: " + targetElemt.isEnabled());

			disableBtn.click();
			Assert.assertFalse(targetElemt.isEnabled());
			System.out.println("Element Status, After 1st Click :: " + targetElemt.isEnabled());

			disableBtn.click();
			Assert.assertTrue(targetElemt.isEnabled());
			System.out.println("Element Status, After 2nd Click :: " + targetElemt.isEnabled());
		}

		//@Test
		public void verifyElementDisplayedTest() {
			Assert.assertEquals(driver.getTitle(), "jQuery UI");

			// Click on Show Link
			driver.findElement(By.linkText("Show")).click();
			Assert.assertEquals(driver.getTitle(), "Show | jQuery UI");

			driver.switchTo().frame(driver.findElement(By.className("demo-frame")));

			// Verify WebElemnt is Displayed
			WebElement elementtoverify = driver.findElement(By.xpath("//div[@id='effect']/h3"));
			Assert.assertFalse(elementtoverify.isDisplayed());
			System.out.println("Element Initial State : " + elementtoverify.isDisplayed());

			driver.findElement(By.id("button")).click();
			Assert.assertTrue(elementtoverify.isDisplayed());
			System.out.println("Element Final State : " + elementtoverify.isDisplayed());
		}


public WebDriver driver= null;
	
	@BeforeMethod
	public void LaunchBrowser() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(45));
		driver.get("https://facebook.com/");
//		driver.get("https://www.w3schools.com/tags/tryit.asp?filename=tryhtml_select_multiple");
}
	
	@AfterMethod
	public void finish() {
		driver.quit();
	}
}
