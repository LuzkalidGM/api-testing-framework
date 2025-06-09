package com.example.tests;

import com.example.framework.BaseApiTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class JsonSchemaValidationTests extends BaseApiTest {
    
    @Test
    public void testUserResponseSchema() {
        Response response = given()
            .spec(requestSpec)
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .extract().response();
            
        // Validate against JSON schema
        response.then().assertThat()
            .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }
    
    @Test
    public void testProductsListSchema() {
        given()
            .spec(requestSpec)
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/products-list-schema.json"));
    }
}