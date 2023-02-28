package com.backbase.mastercard.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.backbase.arrangement.integration.outbound.balance.models.BalanceItem;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.mastercard.mapping.BalanceMapper;
import com.backbase.mastercard.util.RequestUtils;
import com.mastercard.openbanking.accounts.ApiException;
import com.mastercard.openbanking.accounts.api.AccountBalancesApi;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBody;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesParamsBody;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {

    private final AccountBalancesApi accountBalancesApi;
    private final BalanceMapper balanceMapper;
    private final Executor executor;
    private final RequestUtils requestUtils;

    public List<BalanceItem> getBalances(Set<String> arrangementIds) {
        return arrangementIds.stream()
            .map(id -> new PostAccountsAccountBalancesParamsBody()
                .accountId(id)
                .requestInfo(requestUtils.buildBalancesRequestInfo()))
            .map(balanceRequest -> supplyAsync(() -> getArrangementBalance(balanceRequest), executor))
            .collect(Collectors.collectingAndThen(Collectors.toList(), c -> c.stream().map(CompletableFuture::join)))
            .toList();
    }

    private BalanceItem getArrangementBalance(PostAccountsAccountBalancesParamsBody balances) {
        try {
            log.info("Fetching balances for account: {}", balances.getAccountId());
            PostAccountsAccountBalancesOKBody accountBalances = accountBalancesApi.getAccountBalances(balances);
            return balanceMapper.map(accountBalances);
        } catch (ApiException e) {
            log.error("Error when fetching balances", e);
            throw new BadRequestException(e);
        }
    }
}
