package com.upc.dentify.clinicmanagementservice.interfaces;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllItemsPerClinicIdQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ItemPerClinicCommandService;
import com.upc.dentify.clinicmanagementservice.domain.services.ItemPerClinicQueryService;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers.CreateItemPerClinicCommandFromResourceAssembler;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers.ItemPerClinicResourceFromEntityAssembler;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers.UpdateItemPerClinicCommandFromResourceAssembler;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.CreateItemPerClinicResource;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ItemPerClinicResource;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.UpdateItemPerClinicResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/items-per-clinics", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Items per clinics", description = "Items per Clinics Endpoint")
public class ItemPerClinicController {

    private final ItemPerClinicCommandService itemPerClinicCommandService;
    private final ItemPerClinicQueryService itemPerClinicQueryService;

    public ItemPerClinicController(ItemPerClinicCommandService itemPerClinicCommandService,
                                   ItemPerClinicQueryService itemPerClinicQueryService) {
        this.itemPerClinicCommandService = itemPerClinicCommandService;
        this.itemPerClinicQueryService = itemPerClinicQueryService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Create item per clinic", description = "Create a new item per clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item per clinic created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Item already exists in clinic"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createItemPerClinic(@RequestBody CreateItemPerClinicResource resource) {
        try {
            CreateItemPerClinicCommand command =
                    CreateItemPerClinicCommandFromResourceAssembler.toCommandFromResource(resource);

            var itemPerClinic = itemPerClinicCommandService.handle(command);

            if (itemPerClinic.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Unable to create ItemPerClinic"));
            }

            // convertir a DTO de salida
            var response = ItemPerClinicResourceFromEntityAssembler.toResourceFromEntity(itemPerClinic.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update item per clinic", description = "Update item per clinic using the ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated item per clinic"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<ItemPerClinicResource> updatePatient(@PathVariable Long id,
                                                               @RequestBody UpdateItemPerClinicResource resource) {

        UpdateItemPerClinicCommand command = UpdateItemPerClinicCommandFromResourceAssembler.toCommandFromResource(resource, id);
        var item = itemPerClinicCommandService.handle(command);

        if(item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var itemResource = ItemPerClinicResourceFromEntityAssembler.toResourceFromEntity(item.get());

        return ResponseEntity.ok(itemResource);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{clinicId}")
    @Operation(summary = "Get all items by clinic ID", description = "Get all items by clinic ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items found"),
            @ApiResponse(responseCode = "404", description = "Items not found")
    })
    public ResponseEntity<List<ItemPerClinicResource>> getAllItemsPerClinic(@PathVariable Long clinicId) {
        var items = itemPerClinicQueryService.handle(new GetAllItemsPerClinicIdQuery(clinicId));

        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = items.stream()
                .map(ItemPerClinicResourceFromEntityAssembler:: toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
