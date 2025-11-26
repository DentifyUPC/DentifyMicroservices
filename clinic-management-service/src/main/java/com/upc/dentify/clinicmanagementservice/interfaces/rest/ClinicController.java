package com.upc.dentify.clinicmanagementservice.interfaces.rest;

import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllClinicsInformationPreRegisterQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ClinicQueryService;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ClinicInformationPreRegister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/clinics", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Clinics", description = "Clinics Endpoint")
public class ClinicController {

    private final ClinicQueryService clinicQueryService;

    public ClinicController(ClinicQueryService clinicQueryService) {
        this.clinicQueryService = clinicQueryService;
    }

    @GetMapping("/clinics-information-pre-register")
    @Operation(summary = "Get all clinics information pre register", description = "Get all clinics information pre register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clinics found"),
            @ApiResponse(responseCode = "404", description = "Clinics not found")
    })
    public ResponseEntity<List<ClinicInformationPreRegister>> getAllClinicsInformationPreRegister() {
        var clinics = clinicQueryService.handle(new GetAllClinicsInformationPreRegisterQuery());

        if (clinics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clinics);
    }
}
