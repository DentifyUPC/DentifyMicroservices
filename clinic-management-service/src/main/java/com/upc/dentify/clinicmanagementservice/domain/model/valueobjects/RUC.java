package com.upc.dentify.clinicmanagementservice.domain.model.valueobjects;

public record RUC(String ruc) {

    public RUC {
        if (ruc == null || ruc.isBlank()) {
            throw new IllegalArgumentException("RUC cannot be null or blank");
        }
        if (!ruc.matches("\\d{11}")) {
            throw new IllegalArgumentException("RUC must have exactly 11 digits");
        }

        String prefix = ruc.substring(0, 2);
        if (!(prefix.equals("10") || prefix.equals("15") || prefix.equals("20"))) {
            throw new IllegalArgumentException("RUC must start with 10, 15, or 20");
        }
    }

    @Override
    public String toString() {
        return ruc;
    }
}
