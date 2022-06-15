package com.automation.resources.azure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestPlan {

    int id;
    String name;
    int revision;
    String areaPath;
    String iteration;
    String state;
    Root rootSuite;
    PlansProject project;
    Links _links;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Root {
        int id;
        String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Links {
        public UrlLink _self;
        public UrlLink clientUrl;
        public UrlLink rootSuite;
    }
}