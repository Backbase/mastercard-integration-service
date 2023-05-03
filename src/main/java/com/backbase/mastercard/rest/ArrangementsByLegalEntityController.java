package com.backbase.mastercard.rest;

import com.backbase.arrangement.integration.outbound.link.api.ArrangementsByLegalEntityApi;
import com.backbase.arrangement.integration.outbound.link.models.ArrangementItem;
import com.backbase.mastercard.service.LegalEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArrangementsByLegalEntityController implements ArrangementsByLegalEntityApi {

    private final LegalEntityService legalEntityService;

    @Override
    public ResponseEntity<List<ArrangementItem>> getArrangementsByLegalEntity(String legalEntityId) {
        return ResponseEntity.ok(legalEntityService.getLegalEntityArrangements());
    }
}
