package com.upc.dentify.servicecatalogservice.interfaces.rest.acl;

import com.upc.dentify.servicecatalogservice.interfaces.acl.ServiceContextFacade;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/acl/service")
public class ServiceAclController {
    private final ServiceContextFacade serviceContextFacade;

    public ServiceAclController(ServiceContextFacade serviceContextFacade) {
        this.serviceContextFacade = serviceContextFacade;
    }

    @GetMapping()
    public ResponseEntity<List<ServiceResource>> getAllServices() {
        List<ServiceResource> services = this.serviceContextFacade.getAllServices();

        if (services.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(services);
    }
}
