package MyTestRunner;

import io.cucumber.testng.*;

@CucumberOptions (
		features = {"src/test/resource/feature"}, //the path of the feature files
		glue={"StepDefination"} //the path of the step definition files

		)
	
public class TestRunner extends AbstractTestNGCucumberTests {

}
