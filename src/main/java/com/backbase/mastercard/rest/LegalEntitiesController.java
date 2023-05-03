package com.backbase.mastercard.rest;

import com.backbase.legalentity.integration.outbound.api.LegalEntitiesApi;
import com.backbase.legalentity.integration.outbound.models.LegalEntityItem;
import com.backbase.mastercard.service.LegalEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LegalEntitiesController implements LegalEntitiesApi {

    private final LegalEntityService legalEntityService;

    @Override
    public ResponseEntity<List<LegalEntityItem>> getLegalEntities(String field, String term, Integer from, String cursor, Integer size) {
        return ResponseEntity.ok(legalEntityService.getLegalEntities());
    }
}
