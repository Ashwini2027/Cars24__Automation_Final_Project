package SeleniumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FileUpload {
	
	public WebDriver driver = null;
	@Test
	public void uplaodFileTest() throws InterruptedException {
		Assert.assertEquals(driver.getTitle(), "The Internet");
		
		//Upload the file
		WebElement fileUploadBtn = driver.findElement(By.id("file-upload"));
		String fileName = "";
		
		fileUploadBtn.sendKeys(fileName);
		Thread.sleep(5000);
		
		WebElement fileSubmitBtn = driver.findElement(By.id("file-submit"));
		fileSubmitBtn.click();
		Thread.sleep(5000);
		
	}

	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://jqueryui.com/");
			}
	@AfterMethod
	public void finished() {
		driver.quit();
	}
	
}
