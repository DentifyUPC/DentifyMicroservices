package com.upc.dentify.practicemanagementservice.interfaces.rest;

import com.upc.dentify.practicemanagementservice.domain.model.queries.GetAllOdontologistByClinicId;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetAllOdontologistByShiftName;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetOdontologistById;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetOdontologistByUserId;
import com.upc.dentify.practicemanagementservice.domain.services.OdontologistCommandService;
import com.upc.dentify.practicemanagementservice.domain.services.OdontologistQueryService;
import com.upc.dentify.practicemanagementservice.interfaces.rest.resources.OdontologistResource;
import com.upc.dentify.practicemanagementservice.interfaces.rest.resources.UpdateOdontologistRequestResource;
import com.upc.dentify.practicemanagementservice.interfaces.rest.transform.OdontologistCommandFromResourceAssembler;
import com.upc.dentify.practicemanagementservice.interfaces.rest.transform.OdontologistResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Odontologist", description = "Odontologist Endpoint")
@RequestMapping(value ="/api/v1/odontologists", produces = MediaType.APPLICATION_JSON_VALUE)
public class OdontologistController {
    private final OdontologistQueryService odontologistQueryService;
    private final OdontologistCommandService odontologistCommandService;

    public OdontologistController(OdontologistQueryService odontologistQueryService, OdontologistCommandService odontologistCommandService) {
        this.odontologistQueryService = odontologistQueryService;
        this.odontologistCommandService = odontologistCommandService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{odontologistId}")
    public ResponseEntity<OdontologistResource> updatePatient(@PathVariable("odontologistId") Long odontologistId, @RequestBody UpdateOdontologistRequestResource requestResource) {
        var command = OdontologistCommandFromResourceAssembler.toCommand(odontologistId, requestResource);
        var result = odontologistCommandService.handle(command);

        return result.map(odontologist -> ResponseEntity.ok(
                OdontologistResourceFromEntityAssembler.toResource(odontologist)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/clinics/{clinicId}/odontologists")
    public List<OdontologistResource> getAllOdontologistByClinicId(@PathVariable("clinicId") Long clinicId) {
        var odontologist = odontologistQueryService.handle(new GetAllOdontologistByClinicId(clinicId));
        return odontologist.stream()
                .map(OdontologistResourceFromEntityAssembler::toResource)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ODONTOLOGIST')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<OdontologistResource> getOdontologistByUserId(@PathVariable("userId") Long userId) {
        var query = new GetOdontologistByUserId(userId);
        return odontologistQueryService.handle(query)
                .map(odontologist -> ResponseEntity.ok(
                        OdontologistResourceFromEntityAssembler.toResource(odontologist)
                )).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{odontologistId}")
    public ResponseEntity<OdontologistResource> getPatientById(@PathVariable("odontologistId") Long odontologistId) {
        var query = new GetOdontologistById(odontologistId);
        return odontologistQueryService.handle(query)
                .map(odontologist -> ResponseEntity.ok(
                        OdontologistResourceFromEntityAssembler.toResource(odontologist)
                )).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @GetMapping("/shift/{shiftName}/odontologists")
    public List<OdontologistResource> getAllOdontologistByShiftName(@PathVariable("shiftName") String shiftName) {
        var odontologist = odontologistQueryService.handle(new GetAllOdontologistByShiftName(shiftName));
        return odontologist.stream()
                .map(OdontologistResourceFromEntityAssembler::toResource)
                .toList();
    }
}