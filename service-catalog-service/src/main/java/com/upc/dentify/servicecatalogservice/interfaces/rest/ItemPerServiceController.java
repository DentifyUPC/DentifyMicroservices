package com.upc.dentify.servicecatalogservice.interfaces.rest;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsRequiredByServiceIdQuery;
import com.upc.dentify.servicecatalogservice.domain.services.ItemPerServiceQueryService;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/items-per-services", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Items per services", description = "Items per services Endpoint")
public class ItemPerServiceController {

    private final ItemPerServiceQueryService itemPerServiceQueryService;

    public ItemPerServiceController(ItemPerServiceQueryService itemPerServiceQueryService) {
        this.itemPerServiceQueryService = itemPerServiceQueryService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{serviceId}")
    @Operation(summary = "Get all items required per service", description = "Get all items required per service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items found"),
            @ApiResponse(responseCode = "404", description = "Items not found")
    })
    public ResponseEntity<List<ItemRequiredResource>> getAllItemsPerServiceId(@PathVariable Long serviceId) {
        var items = itemPerServiceQueryService.handle(new GetAllItemsRequiredByServiceIdQuery(serviceId));

        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = items.stream().toList();

        return ResponseEntity.ok(resources);
    }

}
