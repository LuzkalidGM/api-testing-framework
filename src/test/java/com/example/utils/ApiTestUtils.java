package com.example.utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;

public class ApiTestUtils {
    
    /**
     * Generic method to perform API requests with logging
     */
    public static Response performRequest(String method, String endpoint, 
                                        Object requestBody, String authToken) {
        RequestSpecification request = given()
            .contentType("application/json")
            .log().all();
            
        if (authToken != null) {
            request.header("Authorization", "Bearer " + authToken);
        }
        
        if (requestBody != null) {
            request.body(requestBody);
        }
        
        Response response;
        switch (method.toUpperCase()) {
            case "GET":
                response = request.when().get(endpoint);
                break;
            case "POST":
                response = request.when().post(endpoint);
                break;
            case "PUT":
                response = request.when().put(endpoint);
                break;
            case "DELETE":
                response = request.when().delete(endpoint);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        
        response.then().log().all();
        return response;
    }
    
    /**
     * Extract all field values from JSON response
     */
    public static void printAllResponseFields(Response response) {
        System.out.println("Response Headers:");
        response.getHeaders().forEach(header -> 
            System.out.println(header.getName() + ": " + header.getValue()));
        
        System.out.println("\nResponse Body:");
        System.out.println(response.getBody().asPrettyString());
    }
}