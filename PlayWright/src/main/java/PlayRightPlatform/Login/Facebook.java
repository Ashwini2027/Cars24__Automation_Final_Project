package PlayRightPlatform.Login;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class Facebook {
	
	
	@Test
	public void login() {
	Playwright playwright = Playwright.create();

    Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setArgs(Arrays.asList("--start-maximized")).setHeadless(false).setSlowMo(1000));  
    
      
    BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null)); 

      
    Page page = context.newPage();  

    
    page.navigate("https://www.facebook.com/"); 

     
    System.out.println("Title: " + page.title());

      
     
    page.locator("input[name='email']").fill("test@gmail.com");  
    page.locator("input[name='pass']").fill("Test123");  

    
     
       
    browser.close();  
    playwright.close();
}
}