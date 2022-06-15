package com.automation.resources.azure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRun {

    int id;
    String name;
    String url;
    Boolean isAutomated;
    String state;
    String webAccessUrl;
    PlansProject project;
    Plan plan;
    Tester owner;

    public TestRun(int id) {
        this.id = id;
    }
}