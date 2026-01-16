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

public class HandleRadioButton {
	@Test
	public void verifYRadioButton() throws InterruptedException {
		Assert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");
		
		//Click Create Account Button
		driver.findElement(By.xpath("//*[@data-testid='open-registration-form-button']")).click();
		
		//Handle Radio Buttons
		List<WebElement> radioButtons = driver.findElements(By.name("sex"));
		for (WebElement button : radioButtons) {
			System.out.println("Is Radio Button Selected : " + button.isSelected());
		}
		
		radioButtons.get(1).click();
		for (WebElement button : radioButtons) {
			System.out.println("Is Radio Button Selected : " + button.isSelected());
		}
		
		for (WebElement button : radioButtons) {
			button.click();
			Thread.sleep(2000);
		}
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
