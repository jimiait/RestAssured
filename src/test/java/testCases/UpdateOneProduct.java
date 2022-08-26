package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateOneProduct {
	SoftAssert softAssert = new SoftAssert();

	String baseUri = "https://techfios.com/api-prod/api/product";
	
	String createProductRequestHeader = "application/json; charset=UTF-8";
	String bearerToken = "Bearer LKJFKJLK544BJLKBM4";
	String createProductResponseHeader;
	String createProductActualMessage;
	String createProductExpectedMessage = "Product was created.";
	
	String firstProductID;
	
	HashMap<String,String> createPayloadMap;
	
	HashMap<String,String> updatePayloadMap;
	String upateProductRequestHeader = "application/json; charset=UTF-8";
	String updateProductResponseHeader;
	String updateProductActualMessage;
	String updateProductExpectedMessage = "Product was updated.";
	
	public Map<String,String> getCreatePayloadMap(){
		
		createPayloadMap = new HashMap<String,String>();
		createPayloadMap.put("name", "Martin F-22 Raptor By USA");
		createPayloadMap.put("description","The best Fighter Jet.");
		createPayloadMap.put("price", "2000");
		createPayloadMap.put("category_id", "2");
		createPayloadMap.put("category_name", "Electronics");
		
		return createPayloadMap;	
	}
	
	public Map<String,String> getUpdatePayloadMap(){
		
		updatePayloadMap = new HashMap<String,String>();
		updatePayloadMap.put("id", firstProductID);
		updatePayloadMap.put("name", "Martin F-16 By USA");
		updatePayloadMap.put("description","The best Fighter Jet.");
		updatePayloadMap.put("price", "1000");
		updatePayloadMap.put("category_id", "2");
		updatePayloadMap.put("category_name", "Electronics");
		
		return updatePayloadMap;	
	}

	@Test(priority=1)
	public void createOneProduct() {
		Response response =

				given()
					.baseUri(baseUri)
					.header("Content-Type", createProductRequestHeader)
					.header("Authorization", bearerToken)
//					.body(new File("src\\main\\java\\data\\CreatePayload.json")).
					.body(getCreatePayloadMap()).
				when()
					.post("/create.php").
				then()
					.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		softAssert.assertEquals(statusCode, 201, "Status codes are not matching!");

		createProductResponseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header Content-Type: " + createProductResponseHeader);
		softAssert.assertEquals(createProductResponseHeader, createProductRequestHeader, "Response Header are not matching!");

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		createProductActualMessage = jsonPath.getString("message");
		System.out.println("Product Message: " + createProductActualMessage);
		softAssert.assertEquals(createProductActualMessage,createProductExpectedMessage, "Product messages are not matching!");
		
		softAssert.assertAll();
	}
	
	@Test(priority=2)
	public void readAllProducts() {
		Response response = 

				given()
					.baseUri(baseUri)
					.header("Content-Type", "application/json; charset=UTF-8")
					.auth().preemptive().basic("demo@techfios.com", "abc123").
				when().get("/read.php").
				then().extract().response();

		String responseBody = response.getBody().asString();	
		
		JsonPath jsonPath = new JsonPath(responseBody);
		
		firstProductID = jsonPath.getString("records[0].id");
		System.out.println("First Product ID:" + firstProductID);

	}
	
	
	@Test(priority=3)
	public void updateOneProduct() {
		Response response =

				given()
					.baseUri(baseUri)
					.header("Content-Type", createProductRequestHeader)
					.header("Authorization", bearerToken)
//					.body(new File("src\\main\\java\\data\\CreatePayload.json")).
					.body(getUpdatePayloadMap()).
				when()
					.put("/update.php").
				then()
					.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching!");

		updateProductResponseHeader = response.getHeader("Content-Type");
		System.out.println("Update Product Header Content-Type: " + updateProductResponseHeader);
		softAssert.assertEquals(updateProductResponseHeader, upateProductRequestHeader, "Update Product Header are not matching!");

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		updateProductActualMessage = jsonPath.getString("message");
		System.out.println("Update Product Message: " + updateProductActualMessage);
		softAssert.assertEquals(updateProductActualMessage,updateProductExpectedMessage, "Update Product messages are not matching!");
		
		softAssert.assertAll();
	}
	
	
	@Test(priority=4)
	public void readOneProduct() {
		Response response =

				given()
					.baseUri(baseUri)
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer LKJFKJLK544BJLKBM4")
					.queryParam("id", firstProductID).
				when()
					.get("/read_one.php").
				then()
					.extract().response();

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		String actualProductNameFromResponse = jsonPath.getString("name");
		System.out.println("Product Name: " + actualProductNameFromResponse);
		softAssert.assertEquals(actualProductNameFromResponse, getUpdatePayloadMap().get("name"), "Product Names are not matching!");
		
		String productDescription = jsonPath.getString("description");
		System.out.println("Product Description: " + productDescription);
		softAssert.assertEquals(productDescription, getUpdatePayloadMap().get("description"), "Product Description are not matching!");
		
		String productPrice = jsonPath.getString("price");
		System.out.println("Product Price: " + productPrice);
		softAssert.assertEquals(productPrice, getUpdatePayloadMap().get("price"), "Product Prices are not matching!");
		
		String productCategoryId = jsonPath.getString("category_id");
		System.out.println("Product Category Id: " + productCategoryId);
		softAssert.assertEquals(productCategoryId, getUpdatePayloadMap().get("category_id"), "Product Category Id are not matching!");
		
		softAssert.assertAll();

	}


}
