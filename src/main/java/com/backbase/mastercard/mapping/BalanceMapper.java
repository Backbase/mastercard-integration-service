package com.backbase.mastercard.mapping;

import com.backbase.arrangement.integration.outbound.balance.models.BalanceItem;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBody;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBodyAccount;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBodyAccountBalancesItems;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    String CURRENT_BALANCE_INDICATOR = "Current";
    String AVAILABLE_BALANCE_INDICATOR = "Available";

    default BalanceItem map(PostAccountsAccountBalancesOKBody accountBalances) {
        PostAccountsAccountBalancesOKBodyAccount account = accountBalances.getAccount();
        List<PostAccountsAccountBalancesOKBodyAccountBalancesItems> balances = account.getBalances();
        BalanceItem balance = new BalanceItem().arrangementId(account.getResourceId());
        calcBalance(balances, CURRENT_BALANCE_INDICATOR).ifPresent(balance::setBookedBalance);
        calcBalance(balances, AVAILABLE_BALANCE_INDICATOR).ifPresent(balance::setAvailableBalance);
        return balance;
    }

    @NotNull
    private static Optional<BigDecimal> calcBalance(
        List<PostAccountsAccountBalancesOKBodyAccountBalancesItems> balances,
        String balanceIndicator) {
        return balances.stream()
            .filter(b -> balanceIndicator.equals(b.getBalanceType()))
            .sorted(Comparator.comparing(PostAccountsAccountBalancesOKBodyAccountBalancesItems::getDateTime))
            .map(b -> b.getBalanceAmount().getAmount())
            .reduce(Double::sum)
            .map(BigDecimal::valueOf);
    }
}
