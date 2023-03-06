package com.backbase.mastercard.model;

import java.util.UUID;
import lombok.Data;

@Data
public class RequestInfo {

    private String consentId = "GFiTpF3:EBy5xGqQMatk";
    private String aspspId = "420e5cff-0e2a-4156-991a-f6eeef0478cf";
    private String psuTppCustomerId = "420e5cff-0e2a-4156-991a-f6eeef0478cf";
    private Boolean isLivePsuRequest = true;
    private String xRequestId = UUID.randomUUID().toString();
    private String psuAgent = "PostmanRuntime/7.20.1";
    private String psuIPAddress;
    private Merchant merchant = new Merchant();

    @Data
    public static class Merchant {

        private String id = "MerchantId";
        private String name = "MerchantName";
    }

}
