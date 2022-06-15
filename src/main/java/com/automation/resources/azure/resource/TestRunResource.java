package com.automation.resources.azure.resource;

import com.automation.resources.azure.domain.RunResult;
import com.automation.resources.azure.domain.TestRequestBody;
import com.automation.resources.azure.domain.TestRun;
import com.automation.resources.azure.domain.TestRunAttachment;
import io.restassured.http.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class TestRunResource extends TestResource {

    public static TestRun get(final Integer runId) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());

        logger(parametersMap);

        return AzureIntegrationSpecs.
                execute(concatenate(TEST_RUN, String.valueOf(runId)), Method.GET, parametersMap).
                as(TestRun.class);
    }

    public static List<RunResult> getResultList(final TestRun run) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());

        logger(parametersMap);

        return AzureIntegrationSpecs.
                execute(concatenate(TEST_RUN, String.valueOf(run.getId()).concat("/").concat("results")), Method.GET, parametersMap).
                jsonPath().
                getList("value", RunResult.class);
    }

    public static RunResult getResult(final Integer runId, final Integer resultId) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());
        logger(parametersMap);

        return AzureIntegrationSpecs.
                execute(concatenate(TEST_RUN, String.valueOf(runId).concat("/").concat("results").concat("/").concat(String.valueOf(resultId))), Method.GET, parametersMap).
                as(RunResult.class);
    }

    public static TestRun create(TestRequestBody requestBody) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());

        final JSONObject json = new JSONObject(requestBody.getRequestUpdateBody());

        logger(json, parametersMap);

        return AzureIntegrationSpecs.
                execute(TEST_RUN, Method.POST, json.toString(), parametersMap).
                as(TestRun.class);
    }

    public static TestRunAttachment upload(final TestRun testRun, Map<String, Object> requestBody) {
        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());
        parametersMap.put("runId", testRun.getId());

        final JSONObject json = new JSONObject(requestBody);

        logger(json, parametersMap);

        return AzureIntegrationSpecs.
                execute(TEST_RUN_ATTACHMENTS, Method.POST, json.toString(), parametersMap).
                as(TestRunAttachment.class);
    }

    public static TestRunAttachment uploadToResult(final TestRun testRun, Map<String, Object> requestBody) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());
        parametersMap.put("runId", testRun.getId());
        parametersMap.put("testCaseResultId", 100000);

        final JSONObject json = new JSONObject(requestBody);

        logger(json, parametersMap);

        return AzureIntegrationSpecs.
                execute(TEST_RUN_RESLT_ATTACHMENTS, Method.POST, json.toString(), parametersMap).
                as(TestRunAttachment.class);
    }

    public static TestRun update(final TestRequestBody testRequestBody) {
        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());

        final JSONObject json = new JSONObject(testRequestBody.getRequestUpdateBody());

        logger(json, parametersMap);

        return AzureIntegrationSpecs.
                execute(concatenate(TEST_RUN, String.valueOf(testRequestBody.getRunId())), Method.PATCH, json.toString(), parametersMap).
                as(TestRun.class);

    }

    public static List<RunResult> updateResult(final Integer runId, final Map<String, Object> requestBody) {

        final Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("organization", configuration.organization());
        parametersMap.put("project", configuration.project());

        JSONArray json = new JSONArray();
        json.put(requestBody);

        logger(json, parametersMap);

        return AzureIntegrationSpecs.
                execute(concatenate(TEST_RUN_RESULTS, String.valueOf(runId).concat("/").concat("results")), Method.PATCH, json.toString(), parametersMap).
                jsonPath().
                getList("value", RunResult.class);
    }

    private static void logger(final JSONArray msg, final Map<String, Object> parametersMap) {

        if (configuration.debug()) {
            System.out.printf("JSON: %s", msg.toString(2));
            logger(parametersMap);
        }
    }

    private static void logger(final JSONObject msg, final Map<String, Object> parametersMap) {

        if (configuration.debug()) {
            System.out.printf("JSON: %s", msg.toString(2));
            logger(parametersMap);
        }
    }

    private static void logger(final Map<String, Object> parametersMap) {
        if (configuration.debug())
            System.out.printf("Parameters: %s", parametersMap);
    }
}