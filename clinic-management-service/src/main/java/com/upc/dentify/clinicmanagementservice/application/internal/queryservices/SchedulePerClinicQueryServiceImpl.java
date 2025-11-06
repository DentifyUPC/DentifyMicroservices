package com.upc.dentify.clinicmanagementservice.application.internal.queryservices;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.SchedulePerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetSchedulePerClinicByClinicIdQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.SchedulePerClinicQueryService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.SchedulePerClinicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SchedulePerClinicQueryServiceImpl implements SchedulePerClinicQueryService {

    private final SchedulePerClinicRepository schedulePerClinicRepository;

    public SchedulePerClinicQueryServiceImpl(SchedulePerClinicRepository schedulePerClinicRepository) {
        this.schedulePerClinicRepository = schedulePerClinicRepository;
    }

    @Override
    public Optional<SchedulePerClinic> handle(GetSchedulePerClinicByClinicIdQuery query) {
        return schedulePerClinicRepository.findByClinicId(query.clinicId());
    }
}
