package com.upc.dentify.clinicmanagementservice.interfaces.rest;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.*;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllShiftsByClinicIdQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ShiftCommandService;
import com.upc.dentify.clinicmanagementservice.domain.services.ShiftQueryService;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers.*;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.*;
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
@RequestMapping(value = "api/v1/shift", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Shift", description = "Shift Endpoint")
public class ShiftController {

    private final ShiftCommandService shiftCommandService;
    private final ShiftQueryService shiftQueryService;

    public ShiftController(ShiftCommandService shiftCommandService, ShiftQueryService shiftQueryService) {
        this.shiftCommandService = shiftCommandService;
        this.shiftQueryService = shiftQueryService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Create shift", description = "Create a new shift in the clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Shift created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Shift already exists in clinic"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createShift(@RequestBody CreateShiftResource resource) {
        try {
            CreateShiftCommand command = CreateShiftCommandFromResourceAssembler.toCommandFromResource(resource);

            var shift = shiftCommandService.handle(command);

            if (shift.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Unable to create shift"));
            }

            var response = ShiftResourceFromEntityAssembler.toResourceFromEntity(shift.get());

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
    @Operation(summary = "Update shift", description = "Update shift using the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated shift"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<ShiftResource> updateShift(@PathVariable Long id,
                                                                 @RequestBody UpdateShiftResource resource) {

        UpdateShiftCommand command = UpdateShiftCommandFromResourceAssembler.toCommandFromResource(id,resource);
        var shift = shiftCommandService.handle(command);

        if(shift.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var shiftResource = ShiftResourceFromEntityAssembler.toResourceFromEntity(shift.get());

        return ResponseEntity.ok(shiftResource);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{clinicId}")
    @Operation(summary = "Get all shifts by clinic id", description = "Get all shifts by clinic id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shifts found"),
            @ApiResponse(responseCode = "404", description = "Shifts not found")
    })
    public ResponseEntity<List<ShiftResource>> getSchedulePerClinic(@PathVariable Long clinicId) {
        var shifts = shiftQueryService.handle(new GetAllShiftsByClinicIdQuery(clinicId));

        if (shifts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var shiftResources = shifts.stream()
                .map(ShiftResourceFromEntityAssembler:: toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(shiftResources);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shift", description = "Delete a shift by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shift deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> deleteShift(@PathVariable Long id) {
        try {
            DeleteShiftCommand command = DeleteShiftCommandFromResourceAssembler.toCommandFromResource(id);
            shiftCommandService.handle(command);

            return ResponseEntity.ok(Map.of("message", "Shift deleted successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }

}
