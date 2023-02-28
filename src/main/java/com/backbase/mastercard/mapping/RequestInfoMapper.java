package com.backbase.mastercard.mapping;

import com.backbase.mastercard.config.AccountsApiProperties.RequestInfo;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesParamsBodyRequestInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestInfoMapper {

    @Mapping(target = "xRequestId", expression = "java(java.util.UUID.randomUUID().toString())")
    PostAccountsAccountBalancesParamsBodyRequestInfo mapBalances(RequestInfo requestInfo);
}
