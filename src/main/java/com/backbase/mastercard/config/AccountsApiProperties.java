package com.backbase.mastercard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mastercard.accounts.api")
public class AccountsApiProperties {

    String baseUri = "https://developer.mastercard.com/apigwproxy/openbanking/connect/api";
    Proxy proxy = new Proxy();

    @Data
    public static class Proxy {

        Boolean enabled = false;
        String host;
        Integer port;
    }

}
