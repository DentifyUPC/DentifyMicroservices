package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.PrescriptionItems;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllPrescriptionItemsByPrescriptionIdQuery;

import java.util.List;

public interface PrescriptionItemQueryService {
    List<PrescriptionItems> handle(GetAllPrescriptionItemsByPrescriptionIdQuery query);
}
