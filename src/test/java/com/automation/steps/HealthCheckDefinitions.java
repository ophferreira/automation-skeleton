package com.automation.steps;

import com.automation.resources.healthCheck.HealthCheck;
import io.cucumber.java.en.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class HealthCheckDefinitions {

    private HealthCheck healthCheck;

    @Given("Should be able to hit the health endpoint")
    public void given_should_be_able_to_hit_the_health_endpoint() {
       healthCheck.healthCheck();
    }
}