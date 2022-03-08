package com.automation.resources.azure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuiteTest {

    int id;
    String name;
    int revision;
    String suiteType;
    String queryString;
    String inheritDefaultConfigurations;
    ParentSuite parentSuite;
    PlansProject project;
    TestPlan plan;
    Links _links;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParentSuite {
        int id;
        String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Links {
        public UrlLink _self;
        public UrlLink testCases;
        public UrlLink testPoints;

    }
}