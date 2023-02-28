package com.backbase.mastercard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mastercard.accounts.api")
public class AccountsApiProperties {

    private String baseUri = "https://developer.mastercard.com/apigwproxy/openbanking/connect/api";

}
