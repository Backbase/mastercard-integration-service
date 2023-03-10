package com.backbase.mastercard.model;

import java.util.UUID;
import lombok.Data;

@Data
public class RequestInfo {

    private String xRequestId = UUID.randomUUID().toString();
    private String aspspId = "420e5cff-0e2a-4156-991a-f6eeef0478cf";
    private String consentId = "GFiTpF3:EBy5xGqQMatk";
    private String psuAgent;
    private String psuIPAddress;

}
