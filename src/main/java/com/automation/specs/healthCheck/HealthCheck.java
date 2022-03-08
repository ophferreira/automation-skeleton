package com.automation.specs.healthCheck;

import com.automation.config.Configuration;
import com.automation.config.ConfigurationManager;
import com.automation.specs.InitialStateSpecs;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;

public class HealthCheck {

    private static final Configuration configuration = ConfigurationManager.getConfiguration();

    private final static String HEADERS_HOST = "headers.Host";

    public static RequestSpecification healthSpec() {

        return new RequestSpecBuilder().
                addRequestSpecification(InitialStateSpecs.set()).
                setBaseUri(configuration.httpbinPath()).
                build();
    }

    public static ResponseSpecification expectedStatusOkay() {

        return new ResponseSpecBuilder().
                expectStatusCode(HttpStatus.SC_OK).
                expectBody(HEADERS_HOST, is(configuration.httpbinPath().getHost())).
                build();
    }
}
