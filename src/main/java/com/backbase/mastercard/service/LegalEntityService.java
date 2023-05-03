package com.backbase.mastercard.service;

import com.backbase.arrangement.integration.outbound.link.models.ArrangementItem;
import com.backbase.legalentity.integration.outbound.models.LegalEntityItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegalEntityService {

    public List<LegalEntityItem> getLegalEntities() {
        return List.of();
    }

    public List<ArrangementItem> getLegalEntityArrangements() {
        return List.of();
    }
}
