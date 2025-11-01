package com.upc.dentify.servicecatalogservice.interfaces.rest.acl;

import com.upc.dentify.servicecatalogservice.interfaces.acl.ItemsContextFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/acl/items")
public class ItemsAclController {

    private final ItemsContextFacade itemsContextFacade;

    public ItemsAclController(ItemsContextFacade itemsContextFacade) {
        this.itemsContextFacade = itemsContextFacade;
    }

    @GetMapping("/{itemId}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long itemId) {
        boolean exists = itemsContextFacade.existsById(itemId);
        return ResponseEntity.ok(exists);
    }
}