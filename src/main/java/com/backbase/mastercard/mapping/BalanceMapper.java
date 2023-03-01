package com.backbase.mastercard.mapping;

import com.backbase.arrangement.integration.outbound.balance.models.BalanceItem;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBodyAccount;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBodyAccountBalancesItems;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBodyAccountBalancesItems.CreditDebitIndicatorEnum;
import com.mastercard.openbanking.accounts.models.PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    String CURRENT_BALANCE_INDICATOR = "Current";
    String AVAILABLE_BALANCE_INDICATOR = "Available";

    default BalanceItem map(PostAccountsAccountBalancesOKBodyAccount account) {
        BalanceItem balance = new BalanceItem().arrangementId(account.getResourceId());
        List<PostAccountsAccountBalancesOKBodyAccountBalancesItems> balances = account.getBalances();
        if (balances != null) {
            calcBalance(balances, CURRENT_BALANCE_INDICATOR).ifPresent(balance::setBookedBalance);
            calcBalance(balances, AVAILABLE_BALANCE_INDICATOR).ifPresent(balance::setAvailableBalance);
        }
        return balance;
    }

    private static Optional<BigDecimal> calcBalance(
        List<PostAccountsAccountBalancesOKBodyAccountBalancesItems> balances,
        String balanceIndicator) {
        return balances.stream()
            .filter(b -> balanceIndicator.equals(b.getBalanceType()) && b.getDateTime() != null)
            .sorted(Comparator.comparing(PostAccountsAccountBalancesOKBodyAccountBalancesItems::getDateTime))
            .reduce(BalanceMapper::calcAmount)
            .map(b -> b.getCreditDebitIndicator() == CreditDebitIndicatorEnum.DEBIT
                ? -b.getBalanceAmount().getAmount()
                : b.getBalanceAmount().getAmount())
            .map(BigDecimal::valueOf);
    }

    private static PostAccountsAccountBalancesOKBodyAccountBalancesItems calcAmount(
        PostAccountsAccountBalancesOKBodyAccountBalancesItems first,
        PostAccountsAccountBalancesOKBodyAccountBalancesItems second) {
        Double amount = first.getCreditDebitIndicator() != second.getCreditDebitIndicator() ?
            first.getBalanceAmount().getAmount() - second.getBalanceAmount().getAmount() :
            first.getBalanceAmount().getAmount() + second.getBalanceAmount().getAmount();
        return new PostAccountsAccountBalancesOKBodyAccountBalancesItems()
            .creditDebitIndicator(first.getCreditDebitIndicator())
            .balanceAmount(new PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount().amount(amount));
    }
}
