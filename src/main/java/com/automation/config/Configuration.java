package com.automation.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

import java.net.URI;

@Config.HotReload
@LoadPolicy(LoadType.MERGE)
@Config.Sources({
        "classpath:config.${ENVIRONMENT}.properties",
        "classpath:config.properties",
        "system:properties",
        "system:env"})
public interface Configuration extends Config {

    @Key("api.base.path")
    public String basePath();

    @Key("api.httpbin.uri")
    public URI httpbinPath();

    @Key("api.base.uri")
    public String baseURI();

    @Key("api.port")
    public int port();

    @Key("api.health.context")
    public String health();

    @Key("log.all")
    boolean logAll();

    @Key("contract.path")
    public String contractPath();

    @Key("contract.extension")
    public String contractExtension();
}