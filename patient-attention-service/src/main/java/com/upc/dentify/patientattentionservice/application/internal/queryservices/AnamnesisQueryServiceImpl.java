package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Anamnesis;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAnamnesisByIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.AnamnesisQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.AnamnesisRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnamnesisQueryServiceImpl implements AnamnesisQueryService {
    private final AnamnesisRepository anamnesisRepository;

    public AnamnesisQueryServiceImpl(AnamnesisRepository anamnesisRepository) {
        this.anamnesisRepository = anamnesisRepository;
    }

    @Override
    public Optional<Anamnesis> handle(GetAnamnesisByIdQuery query) {
        return anamnesisRepository.findById(query.id());
    }
}
