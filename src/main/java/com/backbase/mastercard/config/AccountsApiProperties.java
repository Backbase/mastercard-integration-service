package com.backbase.mastercard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mastercard.accounts.api")
public class AccountsApiProperties {

    String baseUri = "https://developer.mastercard.com/apigwproxy/openbanking/connect/api";
    RequestInfo requestInfo = new RequestInfo();

    @Data
    public static class RequestInfo {

        String consentId = "GFiTpF3:EBy5xGqQMatk";
        String aspspId = "420e5cff-0e2a-4156-991a-f6eeef0478cf";
        Boolean isLivePsuRequest = true;
        String psuTppCustomerId = "420e5cff-0e2a-4156-991a-f6eeef0478cf";
        String psuIPAddress = "192.168.0.1";
        String psuAgent = "PostmanRuntime/7.20.1";
        Merchant merchant = new Merchant();

        @Data
        public static class Merchant {

            private String id = "MerchantId";
            private String name = "MerchantName";
        }
    }

}
