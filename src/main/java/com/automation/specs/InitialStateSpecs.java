package com.automation.specs;

import com.automation.config.Configuration;
import com.automation.config.ConfigurationManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class InitialStateSpecs {

    final static Configuration configuration = ConfigurationManager.getConfiguration();

    public static RequestSpecification set() {

        if (configuration.logAll()) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        } else {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }

        return new RequestSpecBuilder().
                setBaseUri(configuration.baseURI()).
                setBasePath(configuration.basePath()).
                setContentType(ContentType.JSON).
                setAccept(ContentType.JSON).
                setRelaxedHTTPSValidation().
                build();
    }
}