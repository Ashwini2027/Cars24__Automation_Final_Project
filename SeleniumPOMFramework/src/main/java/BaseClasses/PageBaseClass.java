package BaseClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class PageBaseClass {
	
	public WebDriver driver;
	
	/******************************************* Invoke Browser ************************************/
	
	public void invokeBrowser(String browserName) {
		try {
			if (browserName.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver",
						"D:\\\\Eclipse 2025\\\\SeleniumDemoProject\\\\Drivers\\\\chromedriver.exe");
				
				driver = new ChromeDriver();
			} else if (browserName.equalsIgnoreCase("firefox")) {
				System.setProperty("webdriver.chrome.driver",
						"D:\\Eclipse 2025\\SeleniumPOMFramework/Drivers/geckodriver.exe");
				
				driver = new FirefoxDriver();
			} else if (browserName.equalsIgnoreCase("edge")) {
				System.setProperty("webdriver.chrome.driver",
						"D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\msedgedriver.exe");
				driver = new EdgeDriver();
			}		
				else {
				System.setProperty("webdriver.chrome.driver",
						"D:\\Eclipse 2025\\SeleniumDemoProject\\Drivers\\chromedriver.exe");
				driver = new ChromeDriver();
			}
		}
			catch(Exception e)
			{
				System.out.println(e.getMessage());			}
		
		
		}
	}

