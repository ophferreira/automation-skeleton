package com.automation.resources.azure.resource;

import com.automation.resources.azure.domain.SuiteTest;
import com.automation.resources.azure.domain.SuiteTestPoint;
import io.restassured.http.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class TestSuiteResource extends TestResource {

    public static List<SuiteTestPoint> getSuiteTestPoints(final SuiteTest suiteTest) {
        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());
        parametersMap.put("planId", suiteTest.getPlan().getId());
        parametersMap.put("suiteId", suiteTest.getId());

        return AzureIntegrationSpecs.
                execute(TESTPLAN_PLANS_SUITES_TESTPOINT, Method.GET, parametersMap).
                jsonPath().
                getList("value", SuiteTestPoint.class);
    }

    public static SuiteTestPoint getSuiteTestPoint(final SuiteTest suiteTest, final Integer testCaseId) {

        final SuiteTestPoint suiteTestPoint = TestSuiteResource.getSuiteTestPoints(suiteTest).stream()
                .filter(customer -> testCaseId.equals(customer.getTestCase().getId()))
                .findFirst()
                .orElse(new SuiteTestPoint());

        suiteTestPoint.setSuiteTest(suiteTest);

        return suiteTestPoint;
    }

    public static SuiteTest get(final Integer planId, final Integer suiteId) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());
        parametersMap.put("planId", planId);

        return AzureIntegrationSpecs.
                execute(concatenate(TESTPLAN_PLANS_SUITES_DETAILS, String.valueOf(suiteId)), Method.GET, parametersMap).
                as(SuiteTest.class);
    }

    public static List<SuiteTest> getSuites(final Integer planId) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());
        parametersMap.put("planId", planId);

        return AzureIntegrationSpecs.
                execute(TESTPLAN_PLANS_SUITES_DETAILS, Method.GET, parametersMap).
                jsonPath().
                getList("value", SuiteTest.class);
    }
}