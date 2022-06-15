package com.automation.resources.azure.resource;

import com.automation.resources.azure.domain.TestPlan;
import io.restassured.http.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class TestPlanResource extends TestResource {

    public static List<TestPlan> getPlansList() {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());

        return AzureIntegrationSpecs.
                execute(TESTLAN_PLANS, Method.GET, parametersMap).
                jsonPath().
                getList("value", TestPlan.class);
    }

    public static TestPlan get(final Integer planId) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());

        return AzureIntegrationSpecs.
                execute(concatenate(TESTLAN_PLANS, String.valueOf(planId)), Method.GET, parametersMap).
                as(TestPlan.class);
    }

    public static TestPlan getFirst() {

        return TestPlanResource.getPlansList().stream()
                .filter(customer -> configuration.testPlan().equalsIgnoreCase(removerAcentos(customer.getName())))
                .findAny()
                .orElse(new TestPlan());
    }
}