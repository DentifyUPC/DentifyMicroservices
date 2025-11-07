package com.upc.dentify.clinicmanagementservice.interfaces.rest.acl;

import com.upc.dentify.clinicmanagementservice.interfaces.acl.ShiftContextFacade;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ShiftResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/acl/shift")
public class ShiftAclController {
    private final ShiftContextFacade shiftContextFacade;

    public ShiftAclController(ShiftContextFacade shiftContextFacade) {
        this.shiftContextFacade = shiftContextFacade;
    }

    @GetMapping("/clinic/{clinicId}")
    ResponseEntity<List<ShiftResource>> getAllShiftsByClinicId(@PathVariable Long clinicId) {
        List<ShiftResource> shifts = shiftContextFacade.getAllShiftsByClinicId(clinicId);
        if (shifts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shifts);
    }
}
