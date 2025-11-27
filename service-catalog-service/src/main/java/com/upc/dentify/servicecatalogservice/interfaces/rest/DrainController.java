package com.upc.dentify.servicecatalogservice.interfaces.rest;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Endpoint(id = "drain")
public class DrainController {

    private final AtomicBoolean draining = new AtomicBoolean(false);

    @WriteOperation
    public String toggleDrain(Boolean enable) {
        if (enable == null) return "missing 'enable' parameter";

        draining.set(enable);
        return enable ? "drain enabled" : "drain disabled";
    }

    public boolean isDraining() {
        return draining.get();
    }
}