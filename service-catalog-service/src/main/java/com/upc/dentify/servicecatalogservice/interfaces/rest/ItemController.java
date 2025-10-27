package com.upc.dentify.servicecatalogservice.interfaces.rest;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsQuery;
import com.upc.dentify.servicecatalogservice.domain.services.ItemQueryService;
import com.upc.dentify.servicecatalogservice.interfaces.rest.assemblers.ItemResourceFromEntityAssembler;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Items", description = "Items Endpoint")
public class ItemController {

    private final ItemQueryService itemQueryService;

    public ItemController(ItemQueryService itemQueryService) {
        this.itemQueryService = itemQueryService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    @Operation(summary = "Get all items", description = "Get all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items found"),
            @ApiResponse(responseCode = "404", description = "Items not found")
    })
    public ResponseEntity<List<ItemResource>> getAllItems() {
        var items = itemQueryService.handle(new GetAllItemsQuery());

        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = items.stream()
                .map(ItemResourceFromEntityAssembler:: toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
