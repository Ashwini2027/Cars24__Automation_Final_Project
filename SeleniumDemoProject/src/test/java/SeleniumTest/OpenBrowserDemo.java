package SeleniumTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class OpenBrowserDemo {

	@Test
	public void Test1() throws InterruptedException{
		
	// To open chrome  browser
//	System.setProperty("webdriver.chrome.driver", "D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\chromedriver.exe");
//     
//	WebDriver driver = new ChromeDriver();
	System.setProperty("webdriver.firefox.driver", "D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\geckodriver.exe");
    
	WebDriver driver = new FirefoxDriver();  
//     driver.get("https://www.facebook.com/");
	driver.get("https://www.makemytrip.com/flight/search?itinerary=BOM-HKT-12/12/2025&tripType=O&paxType=A-1_C-0_I-0&intl=true&cabinClass=E&lang=eng&cmp=SEM|D|DF|G|Brand|Brand-BrandExact_DT|B_M_Makemytrip_Search_Exact|Brand_Flight_Exact|RSA");
    Thread.sleep(5000);
	driver.getTitle();
//     driver.close();

	}

//	@Test
	public void Test2(){
		
		// To open Firefox browser
		System.setProperty("webdriver.edge.driver", "D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\msedgedriver.exe");
	     
		WebDriver driver = new EdgeDriver();
	     
	     driver.get("https://www.amazon.in/");
	     driver.getTitle();
	     driver.close();

		}
}
