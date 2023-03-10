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

    public static final String CLAIM_ASPSP_ID = "aspspId";
    public static final String CLAIM_CONSENT_ID = "consentId";

    private final InternalRequestContext requestContext;
    private final SecurityContextUtil securityContext;

    public RequestInfo buildRequestInfo() {
        RequestInfo requestInfo = new RequestInfo();
        Optional.ofNullable(requestContext.getRemoteAddress()).ifPresent(requestInfo::setPsuIPAddress);
        Optional.ofNullable(requestContext.getRequestUuid()).ifPresent(requestInfo::setXRequestId);
        Optional.ofNullable(requestContext.getUserAgent()).ifPresent(requestInfo::setPsuAgent);
        securityContext.getUserTokenClaim(CLAIM_ASPSP_ID, String.class).ifPresent(requestInfo::setAspspId);
        securityContext.getUserTokenClaim(CLAIM_CONSENT_ID, String.class).ifPresent(requestInfo::setConsentId);
        return requestInfo;
    }

}
