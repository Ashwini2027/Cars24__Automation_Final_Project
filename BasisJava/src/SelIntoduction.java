import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SelIntoduction implements WebDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "D:\\Eclipse 2025\\chromedriver-win64\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/locatorspractice/");
		  driver.findElement(By.id("inputUsername")).sendKeys("ashwinipatekar20@gmail.com");
		  driver.findElement(By.name("inputPassword")).sendKeys("Ashwini20");
		  driver.findElement(By.className("signInBtn")).click();
	}
 
}
