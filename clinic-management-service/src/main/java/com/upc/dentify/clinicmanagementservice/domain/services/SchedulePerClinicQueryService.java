package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.SchedulePerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetSchedulePerClinicByClinicIdQuery;

import java.util.Optional;

public interface SchedulePerClinicQueryService {
    Optional<SchedulePerClinic> handle(GetSchedulePerClinicByClinicIdQuery query);
}
