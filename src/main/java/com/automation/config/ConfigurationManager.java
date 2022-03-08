package com.automation.config;

import org.aeonbits.owner.ConfigCache;

public class ConfigurationManager {

    private ConfigurationManager() {
    }

    public static Configuration getConfiguration() {
        return ConfigCache.getOrCreate(Configuration.class);
    }

    public static AzureTestPlansConfiguration getAzureTestPlansConfiguration() {
        return ConfigCache.getOrCreate(AzureTestPlansConfiguration.class);
    }
}
