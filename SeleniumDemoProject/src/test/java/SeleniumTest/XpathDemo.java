package SeleniumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class XpathDemo {
	public void FindElements() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\hp\\eclipse-workspace\\SeleniumDemoProject\\drivers\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://www.rediff.com/");
		
		driver.findElement(By.linkText("Sign In")).click();
		
		driver.findElement(By.xpath("//input[@id='login1']")).sendKeys("test@gmail.com");
		
	
	
	
	
	
	}
	
}
