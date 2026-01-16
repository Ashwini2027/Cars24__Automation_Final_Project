package Reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static ExtentReports getExtentReport() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("CARS24 Automation Report");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Project", "CARS24 Automation");
            extent.setSystemInfo("Tester", "Ashwini");
            extent.setSystemInfo("Browser", "Chrome");
        }
        return extent;
    }
}
