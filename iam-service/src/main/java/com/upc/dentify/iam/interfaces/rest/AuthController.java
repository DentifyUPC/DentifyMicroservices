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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user created"),
            @ApiResponse(responseCode = "400", description = "user not created")
    })
    public ResponseEntity<RegisterResponseResource> register(@RequestBody RegisterRequestResource registerRequestResource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(registerRequestResource);
        var response = authService.register(signUpCommand);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @PostMapping("/debug-echo")
    public ResponseEntity<Map<String,Object>> debug(HttpServletRequest req, @RequestBody(required=false) Map<String,Object> body) {
        Map<String,Object> info = new LinkedHashMap<>();
        Collections.list(req.getHeaderNames()).forEach(h -> info.put(h, req.getHeader(h)));
        info.put("parsedBody", body);
        return ResponseEntity.ok(info);
    }

}
