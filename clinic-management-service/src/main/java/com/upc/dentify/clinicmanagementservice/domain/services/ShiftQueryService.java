package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Shift;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllShiftsByClinicIdQuery;

import java.util.List;

public interface ShiftQueryService {
    List<Shift> handle(GetAllShiftsByClinicIdQuery query);
}
