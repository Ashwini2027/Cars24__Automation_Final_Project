package Execution_order;

import org.testng.annotations.Test;

public class SampleTestngUsage {

	@Test
	public void beforeTest() {
		System.out.println("From BeforeTest annotation in SampleTestngUsage class ..");
	}

	@Test
	public void beforeSuite() {
		System.out.println("From BeforeSuite annotation in SampleTestngUsage class ..");
	}

}
