package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Shift;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllShiftsByClinicIdQuery;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ShiftResource;

import java.util.List;

public interface ShiftQueryService {
    List<Shift> handle(GetAllShiftsByClinicIdQuery query);
    List<ShiftResource> handleDto(GetAllShiftsByClinicIdQuery query);
}
