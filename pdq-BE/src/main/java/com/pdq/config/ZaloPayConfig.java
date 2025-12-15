package com.pdq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZaloPayConfig {

    @Value("${zalopay.app-id}")
    private int appId;

    @Value("${zalopay.key1}")
    private String key1;

    @Value("${zalopay.key2}")
    private String key2;

    @Value("${zalopay.endpoint-create}")
    private String endpointCreate;

    @Value("${zalopay.endpoint-callback}")
    private String callbackUrl;

    public int getAppId() { return appId; }
    public String getKey1() { return key1; }
    public String getKey2() { return key2; }
    public String getEndpointCreate() { return endpointCreate; }
    public String getCallbackUrl() { return callbackUrl; }
}
