package com.upc.dentify.practicemanagementservice.interfaces.acl;

import com.upc.dentify.practicemanagementservice.interfaces.rest.resources.OdontologistExternalResource;

import java.util.List;
import java.util.Optional;

public interface OdontologistContextFacade {
    List<OdontologistExternalResource> getAllOdontologistsByClinicId(Long clinicId);
    Optional<OdontologistExternalResource> getOdontologistById(Long id);
}
