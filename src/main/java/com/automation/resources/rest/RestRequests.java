package com.automation.resources.rest;

import com.automation.specs.InitialStateSpecs;
import io.restassured.http.Method;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RestRequests {

    public Response getRequest(final String apiName, final Method requestType, String paramValue) {

        final Resources resourceAPI = Resources.valueOf(apiName);

        return
                given().
                        spec(InitialStateSpecs.set()).
                        when().
                            request(requestType.toString(), resourceAPI.getResource(), paramValue).
                        then().
                            log().ifError().
                        extract().
                        response();
    }


    public Response getRequest(final String apiName) {

        final Resources resourceAPI = Resources.valueOf(apiName);

        return
                given().
                        spec(InitialStateSpecs.set()).
                        when().
                             get(resourceAPI.getResource()).
                        then().
                            log().ifError().
                        extract().
                        response();
    }
}
