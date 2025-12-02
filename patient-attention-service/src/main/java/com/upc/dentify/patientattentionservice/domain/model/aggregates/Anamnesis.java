package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.dentify.patientattentionservice.domain.model.valueobjects.PhoneNumber;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.crypto.SecureStringConverter;
import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Anamnesis extends AuditableAbstractAggregateRoot<Anamnesis> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Column(name = "clinical_background")
    @Convert(converter = SecureStringConverter.class)
    private String clinicalBackground;

    @Column(name = "low_blood_pressure", nullable = false)
    private Boolean lowBloodPressure;

    @Column(name = "high_blood_pressure", nullable = false)
    private Boolean highBloodPressure;

    @Column(name = "smoke", nullable = false)
    private Boolean smoke;

    @Column(name = "current_medications")
    @Convert(converter = SecureStringConverter.class)
    private String currentMedications;

    @Embedded
    @Column(name = "emergency_contact")
    private PhoneNumber emergencyContact;

    public Anamnesis() {
        this.lowBloodPressure = false;
        this.highBloodPressure = false;
        this.smoke = false;
    }

    public Anamnesis(Long anamnesisId) {
        this.setId(anamnesisId);
    }



    // METODOS PARA EL RUNNER DE RE-ENCRYPT

    public String getAnamnesisText() {
        try {
            return mapper.writeValueAsString(new AnamnesisDTO(
                    clinicalBackground,
                    lowBloodPressure,
                    highBloodPressure,
                    smoke,
                    currentMedications,
                    emergencyContact != null ? emergencyContact.phoneNumber() : null
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing anamnesis", e);
        }
    }

    public void setAnamnesisText(String text) {
        try {
            AnamnesisDTO dto = mapper.readValue(text, AnamnesisDTO.class);

            this.clinicalBackground = dto.clinicalBackground;
            this.lowBloodPressure = dto.lowBloodPressure;
            this.highBloodPressure = dto.highBloodPressure;
            this.smoke = dto.smoke;
            this.currentMedications = dto.currentMedications;

            if (dto.emergencyContactNumber != null) {
                this.emergencyContact = new PhoneNumber(dto.emergencyContactNumber);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error deserializing anamnesis", e);
        }
    }

    // DTO interno para serializar compactamente
    private record AnamnesisDTO(
            String clinicalBackground,
            Boolean lowBloodPressure,
            Boolean highBloodPressure,
            Boolean smoke,
            String currentMedications,
            String emergencyContactNumber
    ) {}
}