public class UserAuthenticationTests {
    
}
package com.example.tests;

import com.example.framework.BaseApiTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserAuthenticationTests extends BaseApiTest {
    
    private String userToken;
    private int userId;
    
    @BeforeClass
    public void setupUserTests() {
        // Override base URL for this test class
        RestAssured.baseURI = REQRES_URL;
    }
    
    @Test(priority = 1)
    public void testUserRegistration() {
        String registrationData = """
            {
                "email": "eve.holt@reqres.in",
                "password": "pistol"
            }
            """;
            
        Response response = given()
            .contentType("application/json")
            .body(registrationData)
        .when()
            .post("/register")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("token", notNullValue())
            .extract().response();
            
        userId = response.path("id");
        userToken = response.path("token");
        
        System.out.println("Registered user ID: " + userId);
        System.out.println("Received token: " + userToken);
    }
    
    @Test(priority = 2)
    public void testUserLogin() {
        String loginData = """
            {
                "email": "eve.holt@reqres.in",
                "password": "cityslicka"
            }
            """;
            
        given()
            .contentType("application/json")
            .body(loginData)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("token", hasLength(greaterThan(10)));
    }
    
    @Test
    public void testInvalidLoginAttempt() {
        String invalidLoginData = """
            {
                "email": "peter@klaven"
            }
            """;
            
        given()
            .contentType("application/json")
            .body(invalidLoginData)
        .when()
            .post("/login")
        .then()
            .statusCode(400)
            .body("error", equalTo("Missing password"));
    }
}