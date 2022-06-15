package com.automation.resources.azure.useCase;

import com.automation.resources.azure.domain.*;
import com.automation.resources.azure.exception.TestPlanUploadException;
import com.automation.resources.azure.exception.TestPlansException;
import com.automation.resources.azure.resource.TestPlanResource;
import com.automation.resources.azure.resource.TestResource;
import com.automation.resources.azure.resource.TestRunResource;
import io.cucumber.plugin.event.TestCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class FinishTestExecutionUseCase {

    public TestRun finish(final TestCase testCase, final TestRun run, final TestRunOutcomeState testRunOutcomeState, final Throwable error) throws TestPlansException {

        TestRun testRun = null;
        try {
            final Integer suiteId = TestResource.getTestSuite(testCase.getTags());
            final Integer testCaseId = TestResource.getTestCase(testCase.getTags());

            final LocalDateTime currentDate = LocalDateTime.now();
            final String describe = String.format(
                    "%s|Suite [ %s ] executada \n- Test case [ %s | %s ]",
                    run.getId(),
                    suiteId,
                    testCaseId,
                    testCase.getName()
            );

            final Map<String, Object> requestUpdateBody = new HashMap<>();
            final Map<String, Object> requestResultBody = new HashMap<>();

            requestUpdateBody.put("state", TestRunState.Completed.name());
            requestUpdateBody.put("comment", describe);

            requestResultBody.put("comment", describe);
            requestResultBody.put("state", TestRunState.Completed.name());

            requestUpdateBody.put("completedDate", currentDate);
            requestResultBody.put("outcome", testRunOutcomeState.name());

            if (!testRunOutcomeState.equals(TestRunOutcomeState.Passed)) {

                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                error.printStackTrace(pw);
                final String sStackTrace = sw.toString();

                this.addAttachment(run, testCase, sStackTrace);

                final String errorMessage = String.format(
                        "%s|Falha durante a execução da suite [ %s ] \n- Test case [ %s | %s ]",
                        run.getId(),
                        suiteId,
                        testCaseId,
                        testCase.getName()
                );
                requestUpdateBody.put("errorMessage", errorMessage);
            }

            final TestRequestBody testRequestBody = TestRequestBody.
                    builder().
                    requestUpdateBody(requestUpdateBody).
                    runId(run.getId()).
                    suiteId(suiteId).
                    testCaseId(testCaseId).
                    build();

            final List<RunResult> getResults = TestRunResource.getResultList(run);

            requestResultBody.put("id", getResults.stream().findFirst().orElse(new RunResult(
                    100000
            )).getId());

            TestRunResource.updateResult(run.getId(), requestResultBody);

            testRun = TestRunResource.update(testRequestBody);

        } catch (TestPlanUploadException e) {
            log.info(e.getMessage());

        } catch (Exception ex) {
            throw new TestPlansException("Falha ao criar uma nova execução", ex.getCause());
        }
        return testRun;
    }

    private void addAttachment(final TestRun testRun, final TestCase testCase, final String attachment) throws TestPlanUploadException {

        try {
            final TestPlan testPlan = TestPlanResource.getFirst();
            final LocalDateTime currentDate = LocalDateTime.now();

            final Map<String, Object> requestBody = new HashMap<>();
            final String textoSerializado = Base64.getEncoder().encodeToString(attachment.getBytes());

            final String comment = String.format(
                    "%s|%s -  %s - %s",
                    testPlan.getId(),
                    testPlan.getName(),
                    testCase.getName(),
                    currentDate
            );

            requestBody.put("comment", comment);
            requestBody.put("attachmentType", "GeneralAttachment");
            requestBody.put("fileName", "stack.txt");
            requestBody.put("stream", textoSerializado);

            TestRunResource.upload(testRun, requestBody);
            TestRunResource.uploadToResult(testRun, requestBody);

        } catch (Exception e) {
            throw new TestPlanUploadException("Falha ao tentar fazer upload do arquivo.");
        }
    }
}