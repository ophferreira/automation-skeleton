package com.automation.steps;

import com.automation.resources.rest.RestRequests;
import com.automation.specs.Project01Specs;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Method;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Project01StepDefinitions {

    private RestRequests resource;
    private Project01Specs specs;
    private Response response;
    private String paramValue;

    @Before
    public void before() {
        resource = new RestRequests();
    }

    @Given("User calls {string} with GET http request")
    public void user_calls_with_get_http_request(final String apiName) {
        response = resource.getRequest(apiName);
        specs = new Project01Specs(response);
    }

    @Then("{string} in response body is {string}")
    public void in_response_body_is(final String value1, final String value2) {
        specs.verifyResponseKeyValues(value1, value2);
    }

    @Then("Status code is {int}")
    public void status_code_is(final Integer expectedCode) {
        specs.verifyResponseStatusValue(expectedCode);
    }

    @And("Validate the schema {string}")
    public void validate_the_schema(final String schemaName) {
        specs.validationSchema(schemaName);
    }

    @When("User calls {string} with {string} http request")
    public void user_calls_request_with_http_request(final String apiName, final String requestType) {

        response = resource.getRequest(apiName, Method.valueOf(requestType), paramValue);
        specs = new Project01Specs(response);

    }

    @Given("an address code {string}")
    public void an_address_code(final String cep) {
        paramValue = cep;
    }
}
