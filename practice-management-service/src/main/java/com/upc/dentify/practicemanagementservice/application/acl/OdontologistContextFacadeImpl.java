package com.upc.dentify.practicemanagementservice.application.acl;

import com.upc.dentify.practicemanagementservice.domain.model.queries.GetAllOdontologistByClinicId;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetOdontologistById;
import com.upc.dentify.practicemanagementservice.domain.services.OdontologistQueryService;
import com.upc.dentify.practicemanagementservice.interfaces.acl.OdontologistContextFacade;
import com.upc.dentify.practicemanagementservice.interfaces.rest.resources.OdontologistExternalResource;
import com.upc.dentify.practicemanagementservice.interfaces.rest.transform.OdontologistExternalResourceFromEntityAssembler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologistContextFacadeImpl implements OdontologistContextFacade {
    private final OdontologistQueryService odontologistQueryService;

    public OdontologistContextFacadeImpl(OdontologistQueryService odontologistQueryService) {
        this.odontologistQueryService = odontologistQueryService;
    }

    @Override
    public List<OdontologistExternalResource> getAllOdontologistsByClinicId(Long clinicId) {
        var query = new GetAllOdontologistByClinicId(clinicId);
        var odontologist = odontologistQueryService.handle(query);
        return odontologist.stream()
                .map(OdontologistExternalResourceFromEntityAssembler::fromEntity)
                .toList();
    }

    @Override
    public Optional<OdontologistExternalResource> getOdontologistById(Long id) {
        var query = new GetOdontologistById(id);
        var odontologist = odontologistQueryService.handle(query);
        return odontologist.map(OdontologistExternalResourceFromEntityAssembler::fromEntity);
    }
}
