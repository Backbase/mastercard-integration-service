package com.backbase.mastercard.rest;

import com.backbase.buildingblocks.backend.security.auth.config.SecurityContextUtil;
import com.backbase.cards.client.api.CardsApi;
import com.backbase.cards.client.models.ActivatePost;
import com.backbase.cards.client.models.AddAuthorizedUserPost;
import com.backbase.cards.client.models.CardHolder;
import com.backbase.cards.client.models.CardItem;
import com.backbase.cards.client.models.CardRequestPost;
import com.backbase.cards.client.models.ChangeLimitsPostItem;
import com.backbase.cards.client.models.Currency;
import com.backbase.cards.client.models.Fee;
import com.backbase.cards.client.models.LockStatusPost;
import com.backbase.cards.client.models.RequestPinPost;
import com.backbase.cards.client.models.RequestReplacementPost;
import com.backbase.cards.client.models.ResetPinPost;
import com.backbase.cards.client.models.YearMonth;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardsController implements CardsApi {

    private final SecurityContextUtil securityContextUtil;

    @Override
    public ResponseEntity<CardItem> activate(String id, ActivatePost activatePost) {
        return ResponseEntity.ok(card(id));
    }

    @Override
    public ResponseEntity<Void> addAuthorizedUser(String id, AddAuthorizedUserPost addAuthorizedUserPost) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CardItem> changeLimits(String id, List<ChangeLimitsPostItem> changeLimitsPostItem) {
        return ResponseEntity.ok(card(id));
    }

    @Override
    public ResponseEntity<CardItem> getCardById(String id) {
        return ResponseEntity.ok(card(id));
    }

    @Override
    public ResponseEntity<Fee> getCardReplacementFee(String id) {
        return ResponseEntity.ok(new Fee(new Currency(BigDecimal.ZERO, "GPB")));
    }

    @Override
    public ResponseEntity<List<CardItem>> getCards(List<String> ids, List<String> status, List<String> types) {
        return ResponseEntity.ok(List.of(card(UUID.randomUUID().toString())));
    }

    @Override
    @PreAuthorize("checkPermission('Personal Finance Management', 'Manage Cards', {'create'})")
    public ResponseEntity<CardItem> postCard(CardRequestPost cardRequestPost) {
        return ResponseEntity.ok(card(cardRequestPost.getArrangementId()));
    }

    @Override
    public ResponseEntity<CardItem> requestPin(String id, RequestPinPost requestPinPost) {
        return ResponseEntity.ok(card(id));
    }

    @Override
    public ResponseEntity<CardItem> requestReplacement(String id, RequestReplacementPost requestReplacementPost) {
        return ResponseEntity.ok(card(id));
    }

    @Override
    public ResponseEntity<CardItem> resetPin(String id, ResetPinPost resetPinPost) {
        return ResponseEntity.ok(card(id));
    }

    @Override
    public ResponseEntity<CardItem> updateLockStatus(String id, LockStatusPost lockStatusPost) {
        return ResponseEntity.ok(card(id));
    }

    private CardItem card(String id) {
        String holderName = securityContextUtil.getUserTokenClaim("sub", String.class)
            .map(StringUtils::capitalize)
            .orElse("Holder");
        return new CardItem()
            .id(id)
            .holder(new CardHolder().name(holderName))
            .currency("GBP")
            .brand("Visa")
            .type("Credit")
            .status("Active")
            .expiryDate(new YearMonth().month("02").year("2027"))
            .maskedNumber("4111");
    }
}
