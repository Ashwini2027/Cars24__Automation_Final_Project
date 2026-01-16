package SeleniumTest.MiniProject;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BasicJavaProject {

	@Test
	public void searchProduct() throws InterruptedException {
		
		// After landing on page search for product.
		WebElement searchBox = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
		searchBox.click();
		searchBox.sendKeys("Digital Watch for women");
		WebElement searchButton =driver.findElement(By.id("nav-search-submit-button"));
		searchButton.click();
		
		// Select a particular brand and verify result.
		
		List <WebElement> brandCheckBoxes = driver.findElements(By.cssSelector("#brandsRefinements input[type='checkbox']"));
		
		if(!brandCheckBoxes.isEmpty()) {
			brandCheckBoxes.get(0).click();
			brandCheckBoxes.get(3).click();
		}
		
				
		
		// Apply waits to see the automated results.
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//		wait.until(ExpectedConditions.elementToBeSelected(checkBox));
		//driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				
		//Thread.sleep(5000);
		
		//Select Price range using Drag and drop.
		WebElement dragable = driver.findElement(By.className("s-range-input"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("s-range-input")));
		
		int dragableWidth = dragable.getSize().getWidth();
		int moveBy = -(dragableWidth/2);
		
		Thread.sleep(3000);
		Actions action = new Actions(driver);
		action.clickAndHold(dragable).moveByOffset(moveBy, 0).release().perform();
		
		WebElement goButton = driver.findElement(By.xpath("//input[@class='a-button-input']"));
		goButton.click();
		
		
		
		
		
		
	}
	
	WebDriver driver;
	
	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver",
				"D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\chromedriver.exe");

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://www.amazon.in/");
		
			}
	
	@AfterMethod
	public void finish() {
		driver.quit();
        
	}
	
	
}
