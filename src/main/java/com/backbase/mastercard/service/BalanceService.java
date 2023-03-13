package com.backbase.mastercard.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.backbase.arrangement.integration.outbound.balance.models.BalanceItem;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.mastercard.mapping.BalanceMapper;
import com.backbase.mastercard.util.RequestUtils;
import com.mastercard.mcob.ais.api.AccountBalancesApi;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBody;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesParamsBody;
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
    private final Executor asyncExecutor;
    private final RequestUtils requestUtils;

    public List<BalanceItem> getBalances(Set<String> arrangementIds) {
        var requestInfo = balanceMapper.mapRequestInfo(requestUtils.buildRequestInfo());
        return arrangementIds.stream()
            .map(id -> new PostAccountsAccountBalancesParamsBody()
                .accountId(id)
                .requestInfo(requestInfo))
            .map(balanceRequest -> supplyAsync(() -> getArrangementBalance(balanceRequest), asyncExecutor)
                .exceptionally(
                    e -> {
                        log.error("Error when fetching balances for account", e);
                        throw (RuntimeException) e;
                    }))
            .collect(Collectors.collectingAndThen(Collectors.toList(), c -> c.stream().map(CompletableFuture::join)))
            .toList();
    }

    private BalanceItem getArrangementBalance(PostAccountsAccountBalancesParamsBody balances) {
        try {
            log.debug("Fetching balances for account: {}", balances.getAccountId());
            PostAccountsAccountBalancesOKBody accountBalances = accountBalancesApi.getAccountBalances(balances);
            if (accountBalances.getAccount() == null) {
                throw new NotFoundException().withMessage("Balances not found for account: " + balances.getAccountId());
            }
            return balanceMapper.mapResponse(accountBalances.getAccount());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException(e)
                .withMessage("Error when fetching balances for account: " + balances.getAccountId());
        }
    }
}
