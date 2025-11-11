package com.upc.dentify.practicemanagementservice.domain.services;

import com.upc.dentify.practicemanagementservice.domain.model.aggregates.Odontologist;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetAllOdontologistByClinicId;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetAllOdontologistByShiftName;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetOdontologistById;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetOdontologistByUserId;

import java.util.List;
import java.util.Optional;

public interface OdontologistQueryService {
    List<Odontologist> handle(GetAllOdontologistByClinicId query);
    Optional<Odontologist> handle(GetOdontologistById query);
    Optional<Odontologist> handle(GetOdontologistByUserId query);
    List<Odontologist> handle(GetAllOdontologistByShiftName query);
}