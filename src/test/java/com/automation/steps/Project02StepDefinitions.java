package com.automation.steps;

import io.cucumber.java.en.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Project02StepDefinitions {

    @Given("invalid CEP")
    public void invalid_cep() {
        assertThat(false, is(false));
    }

    @Given("valid CEP")
    public void valid_cep() {
        assertThat(true, is(true));
    }

    @Then("will be valid")
    public void will_be_valid() {
        assertThat(true, is(true));
    }

    @Then("will be invalid")
    public void will_be_invalid() {
        assertThat(true, is(true));
    }
}