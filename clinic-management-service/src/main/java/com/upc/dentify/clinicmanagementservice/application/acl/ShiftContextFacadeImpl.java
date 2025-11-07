package com.upc.dentify.clinicmanagementservice.application.acl;

import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllShiftsByClinicIdQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ShiftQueryService;
import com.upc.dentify.clinicmanagementservice.interfaces.acl.ShiftContextFacade;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ShiftResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftContextFacadeImpl implements ShiftContextFacade {
    private final ShiftQueryService shiftQueryService;

    public ShiftContextFacadeImpl(ShiftQueryService shiftQueryService) {
        this.shiftQueryService = shiftQueryService;
    }

    @Override
    public List<ShiftResource> getAllShiftsByClinicId(Long clinicId) {
        var query = new GetAllShiftsByClinicIdQuery(clinicId);
        return shiftQueryService.handleDto(query);
    }
}
