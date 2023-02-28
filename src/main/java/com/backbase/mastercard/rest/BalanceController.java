package com.backbase.mastercard.rest;

import com.backbase.arrangement.integration.outbound.balance.api.BalanceApi;
import com.backbase.arrangement.integration.outbound.balance.models.BalanceItem;
import com.backbase.mastercard.service.BalanceService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BalanceController implements BalanceApi {

    private final BalanceService balanceService;

    @Override
    public ResponseEntity<List<BalanceItem>> getBalances(Set<String> arrangementIds) {
        return ResponseEntity.ok(balanceService.getBalances(arrangementIds));
    }
}
