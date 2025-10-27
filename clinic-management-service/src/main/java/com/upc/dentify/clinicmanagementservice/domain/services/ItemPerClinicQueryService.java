package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ItemPerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllItemsPerClinicIdQuery;

import java.util.List;

public interface ItemPerClinicQueryService {
    List<ItemPerClinic> handle(GetAllItemsPerClinicIdQuery query);
}
