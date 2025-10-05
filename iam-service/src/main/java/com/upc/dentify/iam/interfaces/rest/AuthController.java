package com.upc.dentify.iam.interfaces.rest;

import com.upc.dentify.iam.domain.services.AuthCommandService;
import com.upc.dentify.iam.interfaces.rest.resources.AuthRequestResource;
import com.upc.dentify.iam.interfaces.rest.resources.AuthResponseResource;
import com.upc.dentify.iam.interfaces.rest.resources.RegisterRequestResource;
import com.upc.dentify.iam.interfaces.rest.resources.RegisterResponseResource;
import com.upc.dentify.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.upc.dentify.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Authentication Endpoint")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthCommandService authService;

    public AuthController(AuthCommandService authCommandService) {
        this.authService = authCommandService;
    }

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user found"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    public ResponseEntity<AuthResponseResource> login(@RequestBody AuthRequestResource authRequestResource){
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(authRequestResource);
        var response = authService.login(signInCommand);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseResource> register(@RequestBody RegisterRequestResource registerRequestResource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(registerRequestResource);
        var response = authService.register(signUpCommand);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
