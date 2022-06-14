package com.automation.listeners;

import azure.domain.TestRun;
import azure.domain.TestRunOutcomeState;
import azure.exception.TestPlansException;
import azure.resource.TestResource;
import azure.useCase.FinishTestExecutionUseCase;
import azure.useCase.StartTestExecutionUseCase;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AzureTestPlansListener implements ConcurrentEventListener {

    private TestRun testCaseExecution;
    private StartTestExecutionUseCase startTestExecutionUseCase;
    private FinishTestExecutionUseCase finishTestExecutionUseCase;

    private Boolean isAzureTestPlansIntegration = false;

    public static boolean checkAzureImplementationEnabled() {

        try {

            TestResource.isAzureImplementationEnabled();

        } catch (Exception e) {

            return false;
        }

        return true;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {

        if (!checkAzureImplementationEnabled()) {
            log.info("Azure Test Plans integration has disabled.");
            return;

        }

        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);

        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
        publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
    }

    private synchronized void handleTestRunStarted(TestRunStarted event) {

        startTestExecutionUseCase = new StartTestExecutionUseCase();
        finishTestExecutionUseCase = new FinishTestExecutionUseCase();
        testCaseExecution = TestRun.builder().build();

        log.info("\n\n █████╗ ███████╗██╗   ██╗██████╗ ███████╗    ████████╗███████╗███████╗████████╗    ██████╗ ██╗      █████╗ ███╗   ██╗███████╗\n" +
                "██╔══██╗╚══███╔╝██║   ██║██╔══██╗██╔════╝    ╚══██╔══╝██╔════╝██╔════╝╚══██╔══╝    ██╔══██╗██║     ██╔══██╗████╗  ██║██╔════╝\n" +
                "███████║  ███╔╝ ██║   ██║██████╔╝█████╗         ██║   █████╗  ███████╗   ██║       ██████╔╝██║     ███████║██╔██╗ ██║███████╗\n" +
                "██╔══██║ ███╔╝  ██║   ██║██╔══██╗██╔══╝         ██║   ██╔══╝  ╚════██║   ██║       ██╔═══╝ ██║     ██╔══██║██║╚██╗██║╚════██║\n" +
                "██║  ██║███████╗╚██████╔╝██║  ██║███████╗       ██║   ███████╗███████║   ██║       ██║     ███████╗██║  ██║██║ ╚████║███████║\n" +
                "╚═╝  ╚═╝╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝       ╚═╝   ╚══════╝╚══════╝   ╚═╝       ╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝\n" +
                "                                                                                                         Integration enabled.");

    }

    private synchronized void handleTestRunFinished(TestRunFinished event) {

        startTestExecutionUseCase = null;
        finishTestExecutionUseCase = null;
        testCaseExecution = null;

        log.info("\n +-+-+-+-+-+ +-+-+ +-+-+-+-+\n" +
                " |P|o|w|e|r| |b|y| |C|I|&|T|\n" +
                " +-+-+-+-+-+ +-+-+ +-+-+-+-+\n");

    }

    private synchronized void handleTestCaseStarted(TestCaseStarted event) {
        final TestCase testCase = event.getTestCase();

        // Checa se o scenario está halitado para iteragir com azure
        if (isAzureTestPlansIntegration = TestResource.isAzureImplementation(testCase.getTags())) {

            try {

                // Cria o test case e armazena para ser utilizando na sessao
                this.testCaseExecution = startTestExecutionUseCase.start(testCase);
                log.info("[ {} ] - {}", testCaseExecution.getId(), testCase.getName());

            } catch (TestPlansException e) {

                try {
                    finishTestExecutionUseCase.finish(testCase, testCaseExecution, TestRunOutcomeState.Error, e);
                } catch (TestPlansException ex) {
                    log.info(ex.getMessage());
                }

                log.info(e.getMessage());
            }
        }
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        final TestCase testCase = event.getTestCase();
        final Result result = event.getResult();

        TestRunOutcomeState testRunOutcomeState = TestRunOutcomeState.Passed;
        // Checa se o scenario está halitado para iteragir com azure
        if (isAzureTestPlansIntegration) {

            try {

                // Check o status final do TestCase
                if (!result.getStatus().isOk()) {
                    testRunOutcomeState = TestRunOutcomeState.Failed;
                    log.info("[ {} ] - {} - {}", testCaseExecution.getId(), testCase.getName(), testRunOutcomeState.name());
                }

            } catch (Exception e) {
                testRunOutcomeState = TestRunOutcomeState.Error;

                log.info(e.getMessage());

            } finally {
                // Encerra a execuçao
                try {
                    finishTestExecutionUseCase.finish(testCase, testCaseExecution, testRunOutcomeState, result.getError());
                } catch (TestPlansException e) {
                    log.info(e.getMessage());
                }

                log.info("[ {} ] - {} - {}", testCaseExecution.getId(), testCase.getName(), testRunOutcomeState.name());
            }
        }
    }
}