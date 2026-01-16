package PageClasses;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Constants.LocatorsRepository;

public class UsedCarDetailsPage extends LocatorsRepository {
	public WebDriver driver;
	public WebDriverWait wait;
	
	public UsedCarDetailsPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	

	public String getCarTitle() {
		wait.until(ExpectedConditions.visibilityOf(carTitle));

	    String titleText = carTitle.getText();
	    System.out.println("Car Title is: " + titleText);

       
		return titleText;
    }
	
	public void scrollDownUCDPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,800)");
		System.out.println("Scrolled down till Car Overview box.");
		
				
	}


  }
