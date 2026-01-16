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

public class HandleWebTable {
		WebDriver driver= null;
	
		@Test
		public void webTableOperationsTest() {

			// Verify Page Title
			Assert.assertEquals(driver.getTitle(), "BSE: 83,938.71 | NSE: 25,722.10 - Live Stock Market | BSE NSE Share Prices | Mutual Fund India | Stock Market: Stock Market Today | Share Market Today - Rediff MoneyWiz");

			// Click on Indices link
			driver.findElement(By.linkText("Indices")).click();

			// Verify Page Title
			Assert.assertEquals(driver.getTitle(), "BSE Indices. BSE, NSE, Stock quotes, share market, stock market, stock tips: Rediff Stocks");

			System.out.println("*************Display Number of Rows***************");

			// Display Number of Rows
			List<WebElement> tableRows = driver.findElements(By.xpath("//*[@class='dataTable']/tbody/tr"));
			System.out.println("Total Number of Rows -- " + tableRows.size());

			System.out.println("*************Get Number of Columns***************");

			// Get Number of Columns.
			List<WebElement> tableColumns = driver.findElements(By.xpath("//*[@class='dataTable']/thead/tr/th"));
			System.out.println("Total Number of Columns -- " + tableColumns.size());

			System.out.println("*************Get Data of a 5th Row***************");

			// Get Data of a Specific Row.
			List<WebElement> fifthRow = driver.findElements(By.xpath("//*[@class='dataTable']/tbody/tr[5]/td"));
			for (WebElement rowItem : fifthRow) {
				System.out.println(rowItem.getText());
			}

			System.out.println("*************Get Data of a 1st Column***************");

			// Get Data of a Column.
			List<WebElement> firstColumn = driver.findElements(By.xpath("//*[@class='dataTable']/tbody/tr/td[1]"));
			for (WebElement webElement : firstColumn) {
				System.out.println(webElement.getText());
			}

			System.out.println("*************Get the Complete Data***************");

			// Get the Complete Data.
			List<WebElement> allRows = driver.findElements(By.xpath("//*[@class='dataTable']/tbody/tr"));
			for (WebElement row : allRows) {
				System.out.println(row.getText());
			}

			System.out.println("*************Get Data From Specific Cell***************");

			// Get Data From Specific Cell.
			String cellValue = driver.findElement(By.xpath("//*[@class='dataTable']/tbody/tr[8]/td[3]")).getText();
			System.out.println(cellValue);

		}
		
				
	@BeforeMethod
	public void Setup() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://www.facebook.com/");

	}

	@AfterMethod
	public void finish() {
		driver.quit();
	}
}
