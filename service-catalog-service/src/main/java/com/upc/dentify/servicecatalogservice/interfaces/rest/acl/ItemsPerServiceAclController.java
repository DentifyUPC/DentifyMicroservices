package com.upc.dentify.servicecatalogservice.interfaces.rest.acl;

import com.upc.dentify.servicecatalogservice.interfaces.acl.ItemPerServiceContextFacade;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/acl-service-catalog/items-per-service")
public class ItemsPerServiceAclController {

    private final ItemPerServiceContextFacade itemPerServiceContextFacade;

    public ItemsPerServiceAclController(ItemPerServiceContextFacade itemPerServiceContextFacade) {
        this.itemPerServiceContextFacade = itemPerServiceContextFacade;
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<List<ItemRequiredResource>> getItemsByServiceId(@PathVariable Long serviceId) {
        List<ItemRequiredResource> items = itemPerServiceContextFacade.getItemIdsByServiceId(serviceId);

        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(items);
    }
}