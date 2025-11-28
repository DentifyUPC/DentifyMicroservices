package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.AllergiasMedications;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllergiasMedicationsByIdQuery;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllergiasMedicationsByClinicalRecordIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.AllergiasMedicationsQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.AllergiasMedicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllergiasMedicationsQueryServiceImpl implements AllergiasMedicationsQueryService {
    private final AllergiasMedicationsRepository allergiasMedicationsRepository;

    public AllergiasMedicationsQueryServiceImpl(AllergiasMedicationsRepository allergiasMedicationsRepository) {
        this.allergiasMedicationsRepository = allergiasMedicationsRepository;
    }

    @Override
    public Optional<AllergiasMedications> handle(GetAllergiasMedicationsByIdQuery query) {
        return allergiasMedicationsRepository.findById(query.id());
    }

    @Override
    public List<AllergiasMedications> handle(GetAllergiasMedicationsByClinicalRecordIdQuery query) {
        return allergiasMedicationsRepository.findByClinicalRecordsId(query.clinicalRecordId());
    }
}
