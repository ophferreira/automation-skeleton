package com.automation.resources.azure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuiteTestPoint {

    int id;
    String url;
    String outcome;
    String state;
    Tester assignedTo;
    TestCaseReference testCase;
    SuiteTest suiteTest;
    Last lastTestRun;
    Last lastResult;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tester {
        String id;
        String displayName;
        String uniqueName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Last {
        String id;
        String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Links {
        public UrlLink _self;
        public UrlLink sourcePlan;
        public UrlLink sourceSuite;
        public UrlLink sourceProject;
        public UrlLink testCases;
        public UrlLink run;
        public UrlLink result;
    }
}