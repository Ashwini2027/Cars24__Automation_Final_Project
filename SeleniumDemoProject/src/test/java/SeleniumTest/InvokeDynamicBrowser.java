package SeleniumTest;

import org.testng.annotations.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class InvokeDynamicBrowser {

	/*
	 * Test Case : Open the selenium official site and verify Home Page Title
	 * 
	 * Test Case 2 : Open the Yahoo and verify Home Page Title
	 * 
	 */

	public WebDriver driver;

	@Parameters("browser")
	@BeforeMethod
	public void openBrowser(@Optional("browser") String browser) {

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");
			
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.chrome.driver",
					"D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\chromedriver.exe");
			
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.chrome.driver",
					"D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\msedgedriver.exe");
			driver = new EdgeDriver();
		}
//		} else if (browser.equalsIgnoreCase("safari")) {
//			driver = new SafariDriver();
//		}
			else {
			System.setProperty("webdriver.chrome.driver",
					"D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}

		// Maximize Browser Window
		driver.manage().window().maximize();
	}

	@AfterMethod
	public void quitBrowser() {
		driver.quit();
	}
	
	
	@Test
	public void verifySeleniumSite() throws InterruptedException {

		// Open FaceBook WebPage
		driver.get("https://www.selenium.dev");

		// Verify the Title
		String title = driver.getTitle();
		Assert.assertEquals(title, "Selenium");

		Thread.sleep(3000);
	}

	@Test
	public void verifyYahooSite() throws InterruptedException {

		// Open FaceBook WebPage
		driver.get("https://mvnrepository.com/repos/central");

		// Verify the Title
		String title = driver.getTitle();
		Assert.assertEquals(title, "Maven Repository: Central");

		Thread.sleep(3000);
	}

}
