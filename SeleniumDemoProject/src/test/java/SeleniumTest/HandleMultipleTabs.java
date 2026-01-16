package SeleniumTest;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HandleMultipleTabs {
	
	@Test
	public void getWindowIdTest () throws InterruptedException {
		
		Assert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");
		driver.findElement(By.id("email")).sendKeys("ashwinichaudhari@gmail.com");
		Thread.sleep(3000);

		// Browser Window GUID
		String mainPageID = driver.getWindowHandle();
		System.out.println("Main Page Window ID : " + mainPageID);

		// Click Link which Open in Same Browser Tab
		driver.findElement(By.linkText("Meta Pay")).click();
		
		Set<String> windowIds = driver.getWindowHandles();
		Iterator<String> itr = windowIds.iterator();

		String homePageID = itr.next();
		String metaPayPageID = itr.next();

		driver.switchTo().window(metaPayPageID);
		Thread.sleep(3000);

		
		//Assert.assertEquals(driver.getTitle(), "Meta Pay | Meta");
		String bannerText = driver.findElement(By.linkText("Check availability")).getText();
		System.out.println(bannerText);
		Thread.sleep(5000);
		
		driver.close();
		
		
		driver.switchTo().window(homePageID);
		
		
		Assert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");
		driver.findElement(By.id("email")).sendKeys("shoaibmirajkar80@gmail.com");
		Thread.sleep(3000);

	}

	//@Test
	public void getWindowIDTest() throws InterruptedException {
		Assert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");
		driver.findElement(By.id("email")).sendKeys("shoaibmirajkar80@gmail.com");
		Thread.sleep(3000);

		// Browser Window GUID
		String mainPageID = driver.getWindowHandle();
		System.out.println("Main Page Window ID : " + mainPageID);

		// Click Link which Open in Same Browser Tab
		driver.findElement(By.linkText("Forgotten password?")).click();

		driver.findElement(By.id("identify_email")).sendKeys("shoaibmirajkar80@gmail.com");
		Thread.sleep(3000);

		// Browser Window GUID
		String forfotPasswordPageID = driver.getWindowHandle();
		System.out.println("Forgotten Password Page Window ID : " + forfotPasswordPageID);
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
