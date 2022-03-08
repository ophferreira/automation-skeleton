package com.automation.resources.azure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunResult {
    int id;
    String name;
    String url;
    String outcome;
    String lastUpdatedDate;
    Integer priority;
    PlansProject project;
    Tester lastUpdatedBy;

    public RunResult(int id) {
        this.id = id;
    }

}

