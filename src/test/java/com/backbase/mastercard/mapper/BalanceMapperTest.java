package com.backbase.mastercard.mapper;

import com.backbase.arrangement.integration.outbound.balance.models.BalanceItem;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBodyAccount;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBodyAccountBalancesItems;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBodyAccountBalancesItems.CreditDebitIndicatorEnum;
import com.mastercard.mcob.ais.model.PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BalanceMapperTest {

    BalanceMapper balanceMapper = new BalanceMapperImpl();

    @Test
    void mapTest() {
        BalanceItem balance = balanceMapper.mapResponse(
            new PostAccountsAccountBalancesOKBodyAccount()
                .balances(List.of(
                    new PostAccountsAccountBalancesOKBodyAccountBalancesItems()
                        .dateTime(OffsetDateTime.now())
                        .balanceType(BalanceMapper.AVAILABLE_BALANCE_INDICATOR)
                        .creditDebitIndicator(CreditDebitIndicatorEnum.CREDIT)
                        .balanceAmount(new PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount()
                            .amount(0d)),
                    new PostAccountsAccountBalancesOKBodyAccountBalancesItems()
                        .dateTime(OffsetDateTime.now().plusSeconds(3))
                        .balanceType(BalanceMapper.AVAILABLE_BALANCE_INDICATOR)
                        .creditDebitIndicator(CreditDebitIndicatorEnum.DEBIT)
                        .balanceAmount(new PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount()
                            .amount(100d)),
                    new PostAccountsAccountBalancesOKBodyAccountBalancesItems()
                        .dateTime(OffsetDateTime.now().plusSeconds(5))
                        .balanceType(BalanceMapper.AVAILABLE_BALANCE_INDICATOR)
                        .creditDebitIndicator(CreditDebitIndicatorEnum.CREDIT)
                        .balanceAmount(new PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount()
                            .amount(100d)),
                    new PostAccountsAccountBalancesOKBodyAccountBalancesItems()
                        .dateTime(OffsetDateTime.now().plusSeconds(10))
                        .balanceType(BalanceMapper.AVAILABLE_BALANCE_INDICATOR)
                        .creditDebitIndicator(CreditDebitIndicatorEnum.DEBIT)
                        .balanceAmount(new PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount()
                            .amount(200d)),
                    new PostAccountsAccountBalancesOKBodyAccountBalancesItems()
                        .dateTime(OffsetDateTime.now().plusSeconds(15))
                        .balanceType(BalanceMapper.AVAILABLE_BALANCE_INDICATOR)
                        .creditDebitIndicator(CreditDebitIndicatorEnum.CREDIT)
                        .balanceAmount(new PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount()
                            .amount(100d)),
                    new PostAccountsAccountBalancesOKBodyAccountBalancesItems()
                        .dateTime(OffsetDateTime.now().plusSeconds(15))
                        .balanceType(BalanceMapper.AVAILABLE_BALANCE_INDICATOR)
                        .creditDebitIndicator(CreditDebitIndicatorEnum.CREDIT)
                        .balanceAmount(new PostAccountsAccountBalancesOKBodyAccountBalancesItemsBalanceAmount()
                            .amount(10d))
                )));
        Assertions.assertEquals(BigDecimal.valueOf(-90d), balance.getAvailableBalance());
    }

}
