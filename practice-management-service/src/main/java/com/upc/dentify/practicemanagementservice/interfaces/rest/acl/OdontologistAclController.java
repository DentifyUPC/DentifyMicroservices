package com.upc.dentify.practicemanagementservice.interfaces.rest.acl;

import com.upc.dentify.practicemanagementservice.interfaces.acl.OdontologistContextFacade;
import com.upc.dentify.practicemanagementservice.interfaces.rest.resources.OdontologistExternalResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/acl/odontologist")
public class OdontologistAclController {
    private final OdontologistContextFacade odontologistContextFacade;

    public OdontologistAclController(OdontologistContextFacade odontologistContextFacade) {
        this.odontologistContextFacade = odontologistContextFacade;
    }

    @GetMapping("/clinic/{clinicId}")
    ResponseEntity<List<OdontologistExternalResource>> getAllOdontologistsByClinicId(@PathVariable Long clinicId) {
        List<OdontologistExternalResource> odontologists = odontologistContextFacade.getAllOdontologistsByClinicId(clinicId);
        if (odontologists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(odontologists);
    }

    @GetMapping("/{id}")
    ResponseEntity<OdontologistExternalResource> getOdontologistById(@PathVariable Long id) {
        return odontologistContextFacade.getOdontologistById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
