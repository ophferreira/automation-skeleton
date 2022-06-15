package com.automation.resources.azure.resource;

import com.automation.config.AzureTestPlansConfiguration;
import com.automation.config.ConfigurationManager;
import com.automation.resources.azure.exception.TestPlansException;

import java.text.Normalizer;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TestResource {

    protected final static AzureTestPlansConfiguration configuration = ConfigurationManager.getAzureTestPlansConfiguration();

    protected static String AZURE_BASE_URL = "https://dev.azure.com/{organization}/{project}/";
    protected static String TESTLAN_PLANS = AZURE_BASE_URL.concat("_apis/testplan/plans?api-version=6.1-preview.1");
    protected static String TESTPLAN_PLANS_SUITES_DETAILS = AZURE_BASE_URL.concat("_apis/testplan/Plans/{planId}/suites?api-version=6.1-preview.1");
    protected static String TESTPLAN_PLANS_SUITES_TESTPOINT = AZURE_BASE_URL.concat("_apis/test/plans/{planId}/suites/{suiteId}/points?api-version=6.1-preview.2");
    protected static String TEST_RUN = AZURE_BASE_URL.concat("_apis/test/runs?api-version=6.1-preview.3");
    protected static String TEST_RUN_RESULTS = AZURE_BASE_URL.concat("_apis/test/runs?api-version=6.1-preview.6");
    protected static String TEST_RUN_ATTACHMENTS = AZURE_BASE_URL.concat("_apis/test/runs/{runId}/attachments?api-version=6.1-preview.1");
    protected static String TEST_RUN_RESLT_ATTACHMENTS = AZURE_BASE_URL.concat("_apis/test/runs/{runId}/Results/{testCaseResultId}/attachments?api-version=6.1-preview.1");

    protected static String removerAcentos(String string) {
        if (string != null) {
            string = Normalizer.normalize(string, Normalizer.Form.NFD);
            string = string.replaceAll("[^\\p{ASCII}]", "");
        }
        return string;
    }

    protected static String concatenate(final String urlString, final String extraPath) {
        try {
            final int startIndex = urlString.indexOf("?");
            final String extractApiVersion = urlString.substring(startIndex);
            final String extractUri = urlString.substring(0, urlString.lastIndexOf("?"));

            return extractUri.concat("/").concat(extraPath).concat(extractApiVersion);

        } catch (Exception e) {
            return urlString;
        }
    }

    public static Boolean isAzureImplementation(List<String> sourceTagNames) {

        if (!configuration.isEnabled())
            return false;

        final Matcher matcher = Pattern.compile("(?<=azure)").matcher(sourceTagNames.toString());

        final Matcher matcherSkip = Pattern.compile("(?<=azure:skip)").matcher(sourceTagNames.toString());

        return !matcherSkip.find() && matcher.find();
    }

    public static void isAzureImplementationEnabled() throws TestPlansException {

        if (!configuration.isEnabled())
            throw new TestPlansException("Azure Test Plans integration has bean disabled");

        try {

            TestPlanResource.getPlansList();

        } catch (Exception e) {
            throw new TestPlansException(e.getMessage() + "\n Azure Test Plans integration has bean disabled");
        }
    }

    public static Integer getTestCase(Collection<String> sourceTagNames) {

        final Matcher matcher = Pattern.compile("(?<=testCaseId:)\\d+").matcher(sourceTagNames.toString());

        Integer testCaseId = null;

        if (matcher.find()) {
            testCaseId = Integer.valueOf(matcher.group(0));
        }
        return testCaseId;
    }

    public static Integer getTestSuite(Collection<String> sourceTagNames) {

        final Matcher matcher = Pattern.compile("(?<=suiteId:)\\d+").matcher(sourceTagNames.toString());

        Integer suiteId = null;

        if (matcher.find()) {
            suiteId = Integer.valueOf(matcher.group(0));
        }
        return suiteId;
    }
}