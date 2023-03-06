package com.backbase.mastercard.util;

import com.backbase.buildingblocks.backend.internalrequest.InternalRequestContext;
import com.backbase.buildingblocks.backend.security.auth.config.SecurityContextUtil;
import com.backbase.mastercard.model.RequestInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestUtils {

    public static final String ATTRIBUTE_ASPSP_ID = "aspspId";
    public static final String ATTRIBUTE_CONSENT_ID = "consentId";
    public static final String ATTRIBUTE_PSU_TPP_CUSTOMER_ID = "psuTppCustomerId";

    private final InternalRequestContext requestContext;
    private final SecurityContextUtil securityContext;

    public RequestInfo buildRequestInfo() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setPsuIPAddress(requestContext.getRemoteAddress());
        Optional.ofNullable(requestContext.getRequestUuid()).ifPresent(requestInfo::setXRequestId);
        Optional.ofNullable(requestContext.getUserAgent()).ifPresent(requestInfo::setPsuAgent);
        securityContext.getUserTokenClaim(ATTRIBUTE_ASPSP_ID, String.class).ifPresent(requestInfo::setAspspId);
        securityContext.getUserTokenClaim(ATTRIBUTE_CONSENT_ID, String.class).ifPresent(requestInfo::setConsentId);
        securityContext.getUserTokenClaim(ATTRIBUTE_PSU_TPP_CUSTOMER_ID, String.class)
            .ifPresent(requestInfo::setPsuTppCustomerId);
        return requestInfo;
    }

}
