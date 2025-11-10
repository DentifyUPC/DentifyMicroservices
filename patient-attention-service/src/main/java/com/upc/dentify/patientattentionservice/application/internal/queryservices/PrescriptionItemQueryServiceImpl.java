package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.PrescriptionItems;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllPrescriptionItemsByPrescriptionIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionItemQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionItemQueryServiceImpl implements PrescriptionItemQueryService {
    private final PrescriptionItemRepository prescriptionItemRepository;

    public PrescriptionItemQueryServiceImpl(PrescriptionItemRepository prescriptionItemRepository) {
        this.prescriptionItemRepository = prescriptionItemRepository;
    }

    @Override
    public List<PrescriptionItems> handle(GetAllPrescriptionItemsByPrescriptionIdQuery query) {
        return prescriptionItemRepository.findAllByPrescriptionId(query.prescriptionId());
    }
}
