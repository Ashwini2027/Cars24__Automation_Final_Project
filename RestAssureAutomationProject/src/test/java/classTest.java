import org.testng.Assert;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class classTest() {
	Response response = RestAssured.get("https://reqres.in/api/users?page=2");

	System.out.println(response.getStatusCode());  
	System.out.println(response.getTime());  
	System.out.println(response.getBody().asString());  
	System.out.println(response.getStatusLine());  
	System.out.println(response.getHeader("content-type"));  
	  
	  
	Assert.assertEquals(response.getStatusCode(), 201);
}
