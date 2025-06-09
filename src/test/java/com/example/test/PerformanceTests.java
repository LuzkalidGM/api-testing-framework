package com.example.test;

import com.example.framework.BaseApiTest;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PerformanceTests extends BaseApiTest {
    
    @Test
    public void testApiResponseTime() {
        given()
            .spec(requestSpec)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .time(lessThan(2000L), TimeUnit.MILLISECONDS);
    }
    
    @Test(invocationCount = 10, threadPoolSize = 5)
    public void testApiUnderLoad() {
        given()
            .spec(requestSpec)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .time(lessThan(5000L), TimeUnit.MILLISECONDS)
            .body("size()", greaterThan(0));
    }
}