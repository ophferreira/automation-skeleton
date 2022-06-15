package com.automation.resources.azure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRequestBody {

    public Map<String, Object> requestUpdateBody;
    public Integer runId;
    public Integer suiteId;
    public Integer testCaseId;
}