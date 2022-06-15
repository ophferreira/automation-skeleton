package com.automation.resources.azure.resource;

import com.automation.config.AzureTestPlansConfiguration;
import com.automation.config.ConfigurationManager;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AzureIntegrationSpecs {

    final static AzureTestPlansConfiguration configuration = ConfigurationManager.getAzureTestPlansConfiguration();

    private static RequestSpecification restWrapper() {

        final String pat = configuration.pat();

        if (configuration.debug()) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        } else {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }

        PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
        auth.setUserName("Basic");
        auth.setPassword(pat);

        return new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                setAccept(ContentType.JSON).
                setRelaxedHTTPSValidation().
                setAuth(auth).
                build();
    }

    public static Response execute(final String url, final Method method, final String requestBody, final Map<String, ?> parameters) {
        return given()
                .spec(restWrapper())
                .and()
                .pathParams(parameters)
                .body(requestBody)
                .when()
                .request(method, url)
                .then()
                .extract()
                .response();
    }

    public static Response execute(final String url, final Method method, final Map<String, ?> parameters) {

        return given()
                .spec(restWrapper())
                .and()
                .pathParams(parameters)
                .when()
                .request(method, url)
                .then()
                .extract()
                .response();
    }
}