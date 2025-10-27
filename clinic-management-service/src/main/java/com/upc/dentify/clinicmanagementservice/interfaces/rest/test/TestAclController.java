package com.upc.dentify.clinicmanagementservice.interfaces.rest.test;

import com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl.ExternalItemPerServiceService;
import com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl.ExternalItemService;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ItemRequiredResource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test/acl")
public class TestAclController {

    private final ExternalItemService externalItemService;
    private final ExternalItemPerServiceService externalItemPerServiceService;

    public TestAclController(ExternalItemService externalItemService,
                             ExternalItemPerServiceService externalItemPerServiceService) {
        this.externalItemService = externalItemService;
        this.externalItemPerServiceService = externalItemPerServiceService;
    }

    @GetMapping("/item/{id}/exists")
    public boolean testItemExists(@PathVariable Long id) {
        return externalItemService.existsById(id);
    }

    @GetMapping("/service/{id}/items")
    public List<ItemRequiredResource> testItemsPerService(@PathVariable Long id) {
        return externalItemPerServiceService.getItemsIdsByServiceId(id);
    }
}