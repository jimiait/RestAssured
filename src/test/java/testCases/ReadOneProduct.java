package testCases;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {
	SoftAssert softAssert = new SoftAssert();

	String baseUri = "https://techfios.com/api-prod/api/product";

	@Test
	public void readOneProduct() {

		/*
		 * given: all input
		 * details(baseURI,Headers,Authorization,Payload/Body,QueryParameters) when:
		 * submit api requests(Http method,Endpoint/Resource) then: validate
		 * response(status code, Headers, responseTime, Payload/Body)
		 * https://techfios.com/api-prod/api/product /read_one.php
		 */
		Response response =

				given()
					.baseUri(baseUri)
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer LKJFKJLK544BJLKBM4")
					.queryParam("id", "5430").
				when()
					.get("/read_one.php").
				then()
					.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching!");

		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header Content-Type: " + responseHeader);
		softAssert.assertEquals(responseHeader, "application/json", "Response Header are not matching!");

		String responseBody = response.getBody().asString();
//		System.out.println("Response Body:" + responseBody);

		JsonPath jsonPath = new JsonPath(responseBody);

		// if i need name .records[0].name
		/*
		 * { "id": "5413", "name": "Pure White Linen Perfume", "description":
		 * "Amazing Smell.", "price": "99", "category_id": "1", "category_name":
		 * "Fashion" }
		 */

		String productName = jsonPath.getString("name");
		System.out.println("Product Name: " + productName);
		softAssert.assertEquals(productName, "Martin F-22 Raptor by USA", "Product Names are not matching!");
		
		String productDescription = jsonPath.getString("description");
		System.out.println("Product Description: " + productDescription);
		softAssert.assertEquals(productDescription, "The best Fighter Jet.", "Product Description are not matching!");
		
		String productPrice = jsonPath.getString("price");
		System.out.println("Product Price: " + productPrice);
		softAssert.assertEquals(productPrice, "200", "Product Prices are not matching!");
		
		String productCategoryId = jsonPath.getString("category_id");
		System.out.println("Product Category Id: " + productCategoryId);
		softAssert.assertEquals(productCategoryId, "2", "Product Category Id are not matching!");
		
		softAssert.assertAll();

	}

}
