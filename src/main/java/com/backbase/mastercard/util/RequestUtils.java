package com.backbase.mastercard.util;

import com.backbase.mastercard.config.AccountsApiProperties;
import com.backbase.mastercard.mapping.RequestInfoMapper;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesParamsBodyRequestInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestUtils {

    private final RequestInfoMapper requestInfoMapper;

    private final AccountsApiProperties accountsApiProperties;

    public PostAccountsAccountBalancesParamsBodyRequestInfo buildBalancesRequestInfo() {
        return requestInfoMapper.mapBalances(accountsApiProperties.getRequestInfo());
    }

}
