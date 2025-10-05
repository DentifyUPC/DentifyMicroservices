package com.upc.dentify.iam.interfaces.rest;

import com.upc.dentify.iam.domain.model.queries.GetUserInformationQuery;
import com.upc.dentify.iam.domain.services.ProfileCommandService;
import com.upc.dentify.iam.domain.services.ProfileQueryService;
import com.upc.dentify.iam.interfaces.rest.resources.PersonalInformationResponseResource;
import com.upc.dentify.iam.interfaces.rest.resources.UpdatePasswordRequestResource;
import com.upc.dentify.iam.interfaces.rest.resources.UpdatePersonalInformationRequestResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import static com.upc.dentify.iam.interfaces.rest.transform.ProfileCommandFromResourceAssembler.toCommand;

@RestController
@Tag(name = "Profile", description = "Profile Endpoint")
@RequestMapping(value ="/api/v1/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private final ProfileQueryService profileQueryService;
    private final ProfileCommandService profileCommandService;

    public ProfileController(ProfileQueryService profileQueryService, ProfileCommandService profileCommandService) {
        this.profileQueryService = profileQueryService;
        this.profileCommandService = profileCommandService;
    }


    @GetMapping
    @Operation(summary = "get authenticated user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user found"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    public ResponseEntity<PersonalInformationResponseResource> getProfile() {
        return ResponseEntity.ok(profileQueryService.handle(new GetUserInformationQuery()));
    }

    @PutMapping("/update-information")
    @Operation(summary = "update user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated information successfully"),
            @ApiResponse(responseCode = "400", description = "error updating user information")
    })
    public ResponseEntity<Map<String,String>> updateInformation(@RequestBody UpdatePersonalInformationRequestResource request) {
        profileCommandService.handle(toCommand(request));
        return ResponseEntity.ok(Map.of("message","Information has been updated"));
    }

    @PutMapping("/update-password")
    @Operation(summary = "update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated password successfully"),
            @ApiResponse(responseCode = "400", description = "error updating user password")
    })
    public ResponseEntity<Map<String,String>> updatePassword(@RequestBody UpdatePasswordRequestResource request) {
        profileCommandService.handle(toCommand(request));
        return ResponseEntity.ok(Map.of("message","Password has been updated"));
    }

}

