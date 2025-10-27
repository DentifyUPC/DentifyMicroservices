package com.upc.dentify.clinicmanagementservice.domain.model.valueobjects;

public record Phone(String phone) {

    public Phone(String phone) {
        validate(phone);
        this.phone = phone;
    }

    private void validate(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }

        phone = phone.replaceAll("[\\s-]", "");

        // Validate phone (9 dígits, starts with 9)
        if (phone.matches("^9\\d{8}$")) {
            return;
        }

        // Validate phone landline with area's code (01, 02–09) + 6 o 7 dígits
        if (phone.matches("^(01\\d{7}|0[2-9]\\d{7}|0[2-9]\\d{8})$\n")) {
            return;
        }

        throw new IllegalArgumentException("Invalid phone number format. Must be a valid mobile or landline number in Peru.");
    }

    @Override
    public String toString() {
        return phone;
    }
}
