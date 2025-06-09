package com.example.framework;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import static org.hamcrest.Matchers.lessThan;

public class BaseApiTest {
    
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    protected static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    protected static final String REQRES_URL = "https://reqres.in/api";
    
    @BeforeClass
    public void setupBaseConfiguration() {
        // Global REST-assured configuration
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        // Build request specification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addHeader("User-Agent", "API-Testing-Framework/1.0")
                .build();
        
        // Build response specification
        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(5000L))
                .build();
        
        // Set global specifications
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }
}