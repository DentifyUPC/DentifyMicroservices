package com.upc.dentify.clinicmanagementservice.interfaces.acl;

import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ShiftResource;

import java.util.List;

public interface ShiftContextFacade {
    List<ShiftResource> getAllShiftsByClinicId(Long clinicId);
}
