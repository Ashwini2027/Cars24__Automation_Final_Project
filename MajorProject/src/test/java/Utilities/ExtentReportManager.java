package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
	
	public static ExtentReports getReportInstance()
	{		
		
			String reportName= DateUtils.getTimeStamp()+".html";
			
			ExtentSparkReporter htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "\\target\\ExtentReport\\"+reportName);
			ExtentReports report = new ExtentReports();
			report.attachReporter(htmlReporter);
			
			report.setSystemInfo("OS", "Windows 11");
			report.setSystemInfo("Environment", "Integration Testing");
			report.setSystemInfo("Build Number", "12.5.4");
			report.setSystemInfo("Browser", "Chrome");
						
			htmlReporter.config().setDocumentTitle("Integration Testing Automation Results");
			htmlReporter.config().setReportName("All Integration Test Report");
			
			return report;
	}
}