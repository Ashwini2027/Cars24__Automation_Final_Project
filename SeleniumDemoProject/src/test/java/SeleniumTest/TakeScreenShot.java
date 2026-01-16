package SeleniumTest;

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
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TakeScreenShot {
	
		WebDriver driver = null;

		@Test
		public void captureScreenShot() throws IOException {
			Assert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");

			driver.findElement(By.id("email")).sendKeys("ashwinichaudhari@gmail.com");
			takeScreenShot();

			
		}
		

		// Capture ScreenShot Funcation
		public void takeScreenShot() throws IOException {

			String filepath = System.getProperty("user.dir") + "/screenshots/" + generateFileName();
			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			FileUtils.copyFile(screenshotFile, new File(filepath));
		}

		

		// Generate Screenshot Name
		public String generateFileName() {
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String date = dateformat.format(new Date());

			String filename = date + ".png";

			return filename;
		}

		@BeforeMethod
		public void init() {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");

			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

			driver.get("https://en-gb.facebook.com");

		}

		@AfterMethod
		public void finish() {
			driver.quit();
		}

	}


