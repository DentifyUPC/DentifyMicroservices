package com.upc.dentify.clinicmanagementservice.application.internal.queryservices;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Shift;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllShiftsByClinicIdQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ShiftQueryService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ShiftRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftQueryServiceImpl implements ShiftQueryService {

    private final ShiftRepository shiftRepository;

    public ShiftQueryServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public List<Shift> handle(GetAllShiftsByClinicIdQuery query) {
        return shiftRepository.findAllByClinicId(query.clinicId());
    }
}
