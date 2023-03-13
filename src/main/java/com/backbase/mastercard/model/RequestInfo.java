package com.backbase.mastercard.model;

import java.util.UUID;
import lombok.Data;

@Data
public class RequestInfo {

    private String xRequestId = UUID.randomUUID().toString();
    private String aspspId;
    private String consentId;
    private String psuAgent;
    private String psuIPAddress;

}
