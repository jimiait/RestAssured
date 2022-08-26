package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteOneProduct {
	SoftAssert softAssert = new SoftAssert();

	String baseUri = "https://techfios.com/api-prod/api/product";
	
	String firstProductID;
	HashMap<String,String> deletePayloadMap;
	
	String deleteProductRequestHeader = "application/json; charset=UTF-8";
	String bearerToken = "Bearer LKJFKJLK544BJLKBM4";
	String deleteProductResponseHeader;
	String deleteProductActualMessage;
	String deleteProductExpectedMessage = "Product was deleted.";
	String productDoesNotExist = "Product does not exist.";

	public Map<String,String> getDeletePayloadMap(){
		
		deletePayloadMap = new HashMap<String,String>();
		deletePayloadMap.put("id", "5445");

		return deletePayloadMap;	
	}

	@Test(priority=1)
	public void deleteOneProduct() {
		Response response =

				given()
					.baseUri(baseUri)
					.header("Content-Type", deleteProductRequestHeader)
					.header("Authorization", bearerToken)
//					.body(new File("src\\main\\java\\data\\CreatePayload.json")).
					.body(getDeletePayloadMap()).
				when()
					.delete("/delete.php").
				then()
					.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching!");

		deleteProductResponseHeader = response.getHeader("Content-Type");
		System.out.println("Delete Product Response Header Content-Type: " + deleteProductResponseHeader);
		softAssert.assertEquals(deleteProductResponseHeader, deleteProductRequestHeader, "Delete Product Response Header are not matching!");

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		deleteProductActualMessage = jsonPath.getString("message");
		System.out.println("Product Message: " + deleteProductActualMessage);
		softAssert.assertEquals(deleteProductActualMessage,deleteProductExpectedMessage, "Product messages are not matching!");
		
		softAssert.assertAll();
	}
	
	
	@Test(priority=2)
	public void readOneProduct() {
		Response response =

				given()
					.baseUri(baseUri)
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer LKJFKJLK544BJLKBM4")
					.queryParam("id", getDeletePayloadMap().get("id")).
				when()
					.get("/read_one.php").
				then()
					.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		softAssert.assertEquals(statusCode, 404, "Status codes are not matching!");

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		String actualProductMessage = jsonPath.getString("message");
		System.out.println("Product Message: " + actualProductMessage);
		softAssert.assertEquals(actualProductMessage,productDoesNotExist,"Product messages are not matching!");
		
		softAssert.assertAll();

	}


}
