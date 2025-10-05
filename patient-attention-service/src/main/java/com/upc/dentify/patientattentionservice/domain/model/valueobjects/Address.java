package com.upc.dentify.patientattentionservice.domain.model.valueobjects;

public record Address(String street, String district, String department, String province) {
    public Address() {this(null, null, null, null);}

    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("street cannot be null or empty");
        }
        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException("city cannot be null or empty");
        }
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("state cannot be null or empty");
        }
        if (province == null || province.isBlank()) {
            throw new IllegalArgumentException("code cannot be null or empty");
        }
    }

    public String getAddress() {
        return this.street + " " + this.district + " " + this.province + " " + this.department;
    }
}
