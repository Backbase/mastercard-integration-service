package com.backbase.mastercard.mapping;

import com.backbase.arrangement.integration.outbound.balance.models.BalanceItem;
import com.backbase.mastercard.model.RequestInfo;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBodyAccount;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBodyAccountBalancesItems;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBodyAccountBalancesItems.CreditDebitIndicatorEnum;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesParamsBodyRequestInfo;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    String CURRENT_BALANCE_INDICATOR = "Current";
    String AVAILABLE_BALANCE_INDICATOR = "Available";

    @Mapping(source = "XRequestId", target = "xRequestId")
    PostAccountsAccountBalancesParamsBodyRequestInfo mapRequestInfo(RequestInfo requestInfo);

    default BalanceItem mapResponse(PostAccountsAccountBalancesOKBodyAccount account) {
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
