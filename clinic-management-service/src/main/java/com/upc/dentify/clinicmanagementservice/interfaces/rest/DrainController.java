package com.upc.dentify.clinicmanagementservice.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/admin")
public class DrainController {

    private final AtomicBoolean drain = new AtomicBoolean(false);

    @PostMapping("/drain")
    public ResponseEntity<String> enableDrain() {
        drain.set(true);
        return ResponseEntity.ok("drain enabled");
    }

    @PostMapping("/undrain")
    public ResponseEntity<String> disableDrain() {
        drain.set(false);
        return ResponseEntity.ok("drain disabled");
    }

    public boolean isDraining() {
        return drain.get();
    }
}