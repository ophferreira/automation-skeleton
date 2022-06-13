package com.automation.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

@LoadPolicy(LoadType.MERGE)
@Config.Sources({
        "classpath:config.azure.properties.${ENVIRONMENT}.properties",
        "classpath:config.azure.properties",
        "classpath:config.${ENVIRONMENT}.properties",
        "classpath:config.properties",
        "system:properties",
        "system:env"})
public interface AzureTestPlansConfiguration extends Config {

    @Key("integration.azure.organization")
    public String organization();

    @Key("integration.azure.project")
    public String project();

    @Key("integration.azure.testPlan")
    public String testPlan();

    @Key("integration.azure.pat")
    public String pat();

    @Key("integration.azure.debug")
    boolean debug();

    @Key("integration.azure.enable")
    public Boolean isEnabled();
}