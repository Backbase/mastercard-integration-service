package com.backbase.mastercard.service;

import com.backbase.arrangement.integration.outbound.balance.models.BalanceItem;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.mastercard.mapping.BalanceMapper;
import com.mastercard.openbanking.accounts.ApiException;
import com.mastercard.openbanking.accounts.api.AccountBalancesApi;
import com.mastercard.openbanking.accounts.models.Merchant;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBody;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesParamsBody;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesParamsBodyRequestInfo;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final AccountBalancesApi accountBalancesApi;

    private final BalanceMapper balanceMapper;

    public List<BalanceItem> getBalances(Set<String> arrangementIds) {
        return arrangementIds.stream()
            .map(id -> new PostAccountsAccountBalancesParamsBody()
                .accountId(id)
                .requestInfo(buildRequestInfo()))
            .map(this::getArrangementBalance)
            .toList();
    }

    private BalanceItem getArrangementBalance(PostAccountsAccountBalancesParamsBody balances) {
        try {
            PostAccountsAccountBalancesOKBody accountBalances = accountBalancesApi.getAccountBalances(balances);
            return balanceMapper.map(accountBalances);
        } catch (ApiException e) {
            throw new BadRequestException(e);
        }
    }

    private PostAccountsAccountBalancesParamsBodyRequestInfo buildRequestInfo() {
        return new PostAccountsAccountBalancesParamsBodyRequestInfo()
            .xRequestId(UUID.randomUUID().toString())
            .aspspId("420e5cff-0e2a-4156-991a-f6eeef0478cf")
            .consentId("GFiTpF3:EBy5xGqQMatk")
            .psuTppCustomerId("420e5cff-0e2a-4156-991a-f6eeef0478cf")
            .psuIPAddress("192.168.0.1")
            .psuAgent("PostmanRuntime/7.20.1")
            .isLivePsuRequest(true)
            .merchant(new Merchant().id("MerchantId").name("MerchantName"));
    }
}
