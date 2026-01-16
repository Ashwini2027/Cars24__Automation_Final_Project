package SeleniumTest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ListfindElementsDemo {

	public void FindElements() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://www.globalcareercounsellor.com/moodle/login/index.php");
		
		List<WebElement> textFields = driver.findElements(By.className("form-control"));

		textFields.get(0).sendKeys("shoaibmirajkar80@gmail.com");
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		textFields.get(1).sendKeys("Test@1234");

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<WebElement> NoElementList = driver.findElements(By.className("shoaib"));
		System.out.println(NoElementList);
		driver.quit();

	}

}

