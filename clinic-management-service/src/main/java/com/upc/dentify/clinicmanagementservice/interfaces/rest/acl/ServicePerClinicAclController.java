package com.upc.dentify.clinicmanagementservice.interfaces.rest.acl;

import com.upc.dentify.clinicmanagementservice.interfaces.acl.ServicePerClinicContextFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/acl/service-per-clinic")
public class ServicePerClinicAclController {
    private final ServicePerClinicContextFacade servicePerClinicContextFacade;

    public ServicePerClinicAclController(ServicePerClinicContextFacade servicePerClinicContextFacade) {
        this.servicePerClinicContextFacade = servicePerClinicContextFacade;
    }

    @GetMapping("/clinic/{clinicId}/service/{serviceId}")
    ResponseEntity<Boolean> existsByClinicIdAndServiceId(@PathVariable Long clinicId, @PathVariable Long serviceId) {
        boolean exists = servicePerClinicContextFacade.existsByClinicIdAndServiceId(clinicId, serviceId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/clinic/{clinicId}/service/{serviceId}/price")
    public ResponseEntity<Double> getTotalServicePrice(@PathVariable Long clinicId, @PathVariable Long serviceId) {
        Double price = servicePerClinicContextFacade.getTotalServicePrice(clinicId, serviceId);
        return ResponseEntity.ok(price);
    }
}
