import org.junit.jupiter.api.Test;

public class PutReq {
	@Test
	public void test()
	{
		
		baseURI = "https://reqres.in/api";
		
		
		given();
		get("/users?page=2").
		then().statusCode(200).
		body("data[2].last_name", equalTo("Funke")).
		body("data.id", hasItems(11,12)).
		log().all();
}
	

}
