package SeleniumTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ImplicitWaitDemo {

	public void pageloadwaits() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(45));
		driver.get("https://edition.cnn.com/");
		
		WebElement searchbox = driver.findElement(By.xpath("//textarea[@title='Search']"));
		searchbox.click();

	}

}
