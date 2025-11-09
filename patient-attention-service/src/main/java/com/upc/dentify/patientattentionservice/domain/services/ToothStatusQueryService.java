package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ToothStatus;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllToothStatusQuery;

import java.util.List;

public interface ToothStatusQueryService {
    List<ToothStatus> handle(GetAllToothStatusQuery query);
}
