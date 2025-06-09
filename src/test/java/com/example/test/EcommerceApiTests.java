package com.example.tests;

import com.example.framework.BaseApiTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class EcommerceApiTests extends BaseApiTest {
    
    private int createdProductId;
    private String authToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
    
    @Test(priority = 1)
    public void testGetAllProducts() {
        given()
            .spec(requestSpec)
            .header("Authorization", authToken)
        .when()
            .get("/products")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("title"))
            .body("[0]", hasKey("price"))
            .body("[0].id", instanceOf(Integer.class))
            .body("findAll { it.price > 0 }.size()", greaterThan(0));
    }
    
    @Test(priority = 2)
    public void testGetProductById() {
        int productId = 1;
        
        Response response = given()
            .spec(requestSpec)
            .pathParam("id", productId)
        .when()
            .get("/products/{id}")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("id", equalTo(productId))
            .body("title", notNullValue())
            .body("price", greaterThan(0f))
            .body("category", notNullValue())
            .extract().response();
            
        // Extract and validate specific fields
        String title = response.path("title");
        Float price = response.path("price");
        
        System.out.println("Product: " + title + " - Price: $" + price);
    }
    
    @Test(priority = 3)
    public void testCreateProduct() {
        String productJson = """
            {
                "title": "Test Product",
                "price": 29.99,
                "description": "A test product for API testing",
                "category": "electronics",
                "image": "https://example.com/image.jpg"
            }
            """;
            
        Response response = given()
            .spec(requestSpec)
            .header("Authorization", authToken)
            .body(productJson)
        .when()
            .post("/products")
        .then()
            .spec(responseSpec)
            .statusCode(201)
            .body("title", equalTo("Test Product"))
            .body("price", equalTo(29.99f))
            .body("id", notNullValue())
            .extract().response();
            
        createdProductId = response.path("id");
        System.out.println("Created product with ID: " + createdProductId);
    }
    
    @DataProvider(name = "invalidProductData")
    public Object[][] invalidProductData() {
        return new Object[][] {
            {"", 29.99, "Empty title should fail"},
            {"Valid Title", -10.0, "Negative price should fail"},
            {"Valid Title", 0, "Zero price should fail"}
        };
    }
    
    @Test(dataProvider = "invalidProductData")
    public void testCreateProductWithInvalidData(String title, double price, String description) {
        String invalidProductJson = String.format("""
            {
                "title": "%s",
                "price": %.2f,
                "description": "%s",
                "category": "electronics"
            }
            """, title, price, description);
            
        given()
            .spec(requestSpec)
            .header("Authorization", authToken)
            .body(invalidProductJson)
        .when()
            .post("/products")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)))
            .body("error", notNullValue());
    }
}