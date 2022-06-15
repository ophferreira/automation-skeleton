package com.automation.resources.azure.useCase;

import com.automation.resources.azure.domain.*;
import com.automation.resources.azure.exception.TestPlansException;
import com.automation.resources.azure.resource.TestPlanResource;
import com.automation.resources.azure.resource.TestResource;
import com.automation.resources.azure.resource.TestRunResource;
import com.automation.resources.azure.resource.TestSuiteResource;
import io.cucumber.plugin.event.TestCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class StartTestExecutionUseCase {

    public TestRun createNewExecution(final TestCase testCase) throws TestPlansException {
        TestRun run;
        try {
            final Integer suiteId = TestResource.getTestSuite(testCase.getTags());
            final Integer testCaseId = TestResource.getTestCase(testCase.getTags());

            final TestPlan testPlan = TestPlanResource.getFirst();

            final SuiteTest suiteTest = TestSuiteResource.get(testPlan.getId(), suiteId); // Trabalha com objeto

            final SuiteTestPoint suiteTestPoint = TestSuiteResource.getSuiteTestPoint(suiteTest, testCaseId);

            final Map<String, Object> requestBody = new HashMap<>();
            final Integer[] pointIds = {suiteTestPoint.getId()};

            final Map<String, Integer> plan = new HashMap<>();
            plan.put("id", testPlan.getId());

            final LocalDateTime currentDate = LocalDateTime.now();

            final String name = String.format(
                    "%s|%s - %s|%s",
                    suiteTest.getName().replace("\\s+", ""),
                    suiteTest.getId(),
                    suiteTestPoint.getTestCase().getName().replace("\\s+", ""),
                    suiteTestPoint.getTestCase().getId()

            );
            final String describe = String.format(
                    "%s|%s - %s - %s - %s",
                    testPlan.getName(),
                    testPlan.getId(),
                    name,
                    "via automation on",
                    currentDate
            );

            requestBody.put("name", name);
            requestBody.put("comment", describe);
            requestBody.put("pointIds", pointIds);
            requestBody.put("plan", new JSONObject(plan));
            requestBody.put("automated", "true");
            requestBody.put("controller", "RestAssured");
            requestBody.put("state", TestRunState.NotStarted.name());
            requestBody.put("iteration", testPlan.getIteration());

            final TestRequestBody testRequestBody = TestRequestBody.builder()
                    .requestUpdateBody(requestBody)
                    .testCaseId(suiteTestPoint.getTestCase().getId())
                    .suiteId(suiteTest.getId())
                    .build();

            run = TestRunResource.create(testRequestBody);

        } catch (Exception e) {
            throw new TestPlansException("Falha ao criar uma nova execução", e.getCause());
        }

        return run;
    }

    public TestRun start(final TestCase testCase) throws TestPlansException {

        try {
            final Integer suiteId = TestResource.getTestSuite(testCase.getTags());
            final Integer testCaseId = TestResource.getTestCase(testCase.getTags());

            // Cria um test run
            final TestRun run = createNewExecution(testCase);
            final LocalDateTime currentDate = LocalDateTime.now();
            final String describe = String.format(
                    "%s|Iniciando execução da suite [ %s ] \n- Test case [ %s | %s ]",
                    run.getId(),
                    suiteId,
                    testCaseId,
                    testCase.getName()
            );

            final Map<String, Object> requestUpdateBody = new HashMap<>();
            requestUpdateBody.put("state", TestRunState.InProgress.name());
            requestUpdateBody.put("substate", TestRunSubState.runningTests.name());
            requestUpdateBody.put("comment", describe);
            requestUpdateBody.put("startedDate", currentDate);

            final TestRequestBody testRequestBody = TestRequestBody.
                    builder().
                    requestUpdateBody(requestUpdateBody).
                    runId(run.getId()).
                    suiteId(suiteId).
                    testCaseId(testCaseId).
                    build();

            final Map<String, Object> requestResultBody = new HashMap<>();
            requestResultBody.put("comment", describe);
            requestResultBody.put("outcome", TestRunOutcomeState.InProgress.name());
            requestResultBody.put("state", TestRunState.InProgress.name());

            final List<RunResult> getResults = TestRunResource.getResultList(run);

            requestResultBody.put("id", getResults.stream().findFirst().orElse(new RunResult(
                    100000
            )).getId());

            TestRunResource.updateResult(run.getId(), requestResultBody);

            return TestRunResource.update(testRequestBody);

        } catch (Exception e) {
            throw new TestPlansException("Falha ao inicializar a execução", e.getCause());
        }
    }
}