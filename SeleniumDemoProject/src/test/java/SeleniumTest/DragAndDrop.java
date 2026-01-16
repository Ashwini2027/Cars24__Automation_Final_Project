package SeleniumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class DragAndDrop {
	@Test
	public void dragAndDropHardenTest() throws InterruptedException {
		Assert.assertEquals(driver.getTitle(), "jQuery UI");

		driver.findElement(By.linkText("Droppable")).click();
		Assert.assertEquals(driver.getTitle(), "Droppable | jQuery UI");

		driver.switchTo().frame(driver.findElement(By.className("demo-frame")));
		Thread.sleep(5000);

		WebElement dragableEle = driver.findElement(By.id("draggable"));
		WebElement dropableEle = driver.findElement(By.id("droppable"));

		Actions action = new Actions(driver);
		action.clickAndHold(dragableEle).moveToElement(dropableEle).release().build().perform();

		Thread.sleep(5000);
	}

	// @Test
	public void dragAndDropTest() throws InterruptedException {
		Assert.assertEquals(driver.getTitle(), "jQuery UI");

		driver.findElement(By.linkText("Droppable")).click();
		Assert.assertEquals(driver.getTitle(), "Droppable | jQuery UI");

		driver.switchTo().frame(driver.findElement(By.className("demo-frame")));
		Thread.sleep(5000);

		WebElement dragableEle = driver.findElement(By.id("draggable"));
		WebElement dropableEle = driver.findElement(By.id("droppable"));

		Actions action = new Actions(driver);
		action.dragAndDrop(dragableEle, dropableEle).build().perform();

		Thread.sleep(5000);
	}

	//@Test
	public void dragableTest() throws InterruptedException {
		Assert.assertEquals(driver.getTitle(), "jQuery UI");

		driver.findElement(By.linkText("Draggable")).click();
		Assert.assertEquals(driver.getTitle(), "Draggable | jQuery UI");

		driver.switchTo().frame(driver.findElement(By.className("demo-frame")));
		Thread.sleep(5000);

		WebElement dragable = driver.findElement(By.id("draggable"));

		Actions action = new Actions(driver);
		action.dragAndDropBy(dragable, 100, 100).build().perform();

		Thread.sleep(5000);
	}

	
	public WebDriver driver = null;
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


