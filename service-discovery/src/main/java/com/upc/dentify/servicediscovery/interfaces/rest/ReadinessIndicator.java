package com.upc.dentify.servicediscovery.interfaces.rest;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("readiness")
public class ReadinessIndicator implements HealthIndicator {

    private final DrainController drainController;

    public ReadinessIndicator(DrainController drainController) {
        this.drainController = drainController;
    }

    @Override
    public Health health() {
        if (drainController.isDraining()) {
            return Health.down().withDetail("drain", "true").build();
        }
        return Health.up().build();
    }
}