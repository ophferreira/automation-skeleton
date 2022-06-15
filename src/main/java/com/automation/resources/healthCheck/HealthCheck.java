package com.automation.resources.healthCheck;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class HealthCheck {

    public Response healthCheck() {
        return
                given().
                        spec(com.automation.specs.healthCheck.HealthCheck.healthSpec()).
                        when().
                        get("/get").
                        then().
                        log().ifError().
                        spec(com.automation.specs.healthCheck.HealthCheck.expectedStatusOkay()).
                        extract().
                        response();
    }
}