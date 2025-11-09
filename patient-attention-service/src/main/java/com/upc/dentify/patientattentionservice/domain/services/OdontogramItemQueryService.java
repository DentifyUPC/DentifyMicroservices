package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.OdontogramItem;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllOdontogramItemsByOdontogramIdQuery;

import java.util.List;

public interface OdontogramItemQueryService {
    List<OdontogramItem> handle(GetAllOdontogramItemsByOdontogramIdQuery query);
}
