package FinalTestCase;

import org.testng.annotations.Test;
import BaseClasses.BaseClass;
import PageClasses.BuyCarPage;
import PageClasses.HomePage;
import PageClasses.SellCarPage;
import PageClasses.UsedCarDetailsPage;



@Test

public class Cars24Test  extends BaseClass {
	HomePage homePage;
	SellCarPage sellCarPage;
	BuyCarPage buyCarPage;
	UsedCarDetailsPage usedCarDetailsPage;
	
	public void runCars24Test() throws Exception {

	    test = extent.createTest("CARS24 Buy Car Flow Test");
	    
	    invokeBrowser("chrome");
	    test.info("Browser launched");

	    homePage = openWebsite();
//	    homePage.loginToApplication();
	    test.pass("Opened CARS24 website");

	    sellCarPage = homePage.navigateToSellCarPage();
//	    sellCarPage.sellCarProcess();
	    test.info("Sell car process completed");

	    buyCarPage = sellCarPage.navigateToBuyCarPage();
	    buyCarPage.buyCarProcess();
	    test.pass("Buy Car process completed");

	    usedCarDetailsPage = buyCarPage.navigateToUsedCarDetailsPage();
	    String carTitle = usedCarDetailsPage.getCarTitle();
	    usedCarDetailsPage.scrollDownUCDPage();
	    test.pass("Car title fetched: " + carTitle);
	}
}

