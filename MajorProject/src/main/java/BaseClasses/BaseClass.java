package BaseClasses;
import Reports.ExtentReportManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import PageClasses.HomePage;




public class BaseClass {
	
	
	public WebDriver driver;
	public WebDriverWait waits;
	public HomePage homePage;
	public ExtentReports extent;
	public ExtentTest test;

	
	
	@BeforeSuite
	public void setupReport() {
	    extent = ExtentReportManager.getExtentReport();
	}

	@BeforeMethod
	public void beforeEachTest() {
		waits = new WebDriverWait(driver, Duration.ofSeconds(10));
		
	}
	
	
	public void invokeBrowser(String browserName) throws IOException, InterruptedException {
	
			try {
				if (browserName.equalsIgnoreCase("chrome")) {
					driver = new ChromeDriver();
		
				} else if (browserName.equalsIgnoreCase("firefox")) {
					driver = new FirefoxDriver();
		
				} else if (browserName.equalsIgnoreCase("edge")) {
					driver = new EdgeDriver();
				} else {
					driver = new ChromeDriver();
				}
			} 
			catch(Exception e){
				System.out.println(e.getMessage());			
			}
			
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		System.out.println("Browser is successfully open.");

	}
	
	public WebElement waitForElement(WebElement element) {
	    return new WebDriverWait(driver, Duration.ofSeconds(10))
	            .until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public WebElement waitForVisible(By locator) {
	    return new WebDriverWait(driver, Duration.ofSeconds(10))
	            .until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public HomePage openWebsite() {
		driver.get("https://www.cars24.com/");
		System.out.println("Home Page Title: " + driver.getTitle());
		return PageFactory.initElements(driver, HomePage.class);
	}
	
	public void handleUnexpectedAlerts() throws TimeoutException {
	WebDriverWait shortWaits = new WebDriverWait(driver, Duration.ofSeconds(3));
	Alert alert = shortWaits.until(ExpectedConditions.alertIsPresent());
	alert.dismiss();
	}
	
	public String getText(By locator) {
        return waits.until(ExpectedConditions.visibilityOfElementLocated(locator))
                   .getText().trim();
    }
	
//	public String captureScreenshot(String testName) throws IOException {
//	    TakesScreenshot ts = (TakesScreenshot) driver;
//	    File src = ts.getScreenshotAs(OutputType.FILE);
//
//	    String path = System.getProperty("user.dir") + "/screenshots/" + testName + ".png";
//	    FileUtils.copyFile(src, new File(path));
//	    return path;
//	}

//	 @AfterMethod
	 
	 public void teardown() {
		
		 driver.close();
	 }
	 
	 @AfterSuite
	    public void flushReport() {
	        extent.flush();
	    }
}
