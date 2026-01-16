package FinalTestCase;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

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

		logger=report.createTest("Test One");
	    
		logger.log(Status.INFO, "Initializing the Browser");
	    invokeBrowser("chrome");
        logger.log(Status.PASS, "Browser Initialized Successfully");

	    homePage = openWebsite();
//	    homePage.loginToApplication();
	    logger.log(Status.PASS, "Login Process Completed");

	    sellCarPage = homePage.navigateToSellCarPage();
//	    sellCarPage.sellCarProcess();
	    logger.log(Status.PASS, "Sell Car process completed");

	    buyCarPage = sellCarPage.navigateToBuyCarPage();
	    buyCarPage.buyCarProcess();
	    logger.log(Status.PASS, "Buy Car process completed");

	    usedCarDetailsPage = buyCarPage.navigateToUsedCarDetailsPage();
	    String carTitle = usedCarDetailsPage.getCarTitle();
	    usedCarDetailsPage.scrollDownUCDPage();
	    logger.log(Status.PASS, "Used Car Details process completed");
	}
}

