package com.upc.dentify.practicemanagementservice.domain.model.aggregates;

import com.upc.dentify.practicemanagementservice.domain.model.valueobjects.*;
import com.upc.dentify.practicemanagementservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Entity
@Getter
public class Odontologist extends AuditableAbstractAggregateRoot<Odontologist> {
    @Embedded
    @Column(nullable = false)
    private PersonName personName;

    @Embedded
    @Column(nullable = false)
    private BirthDate birthDate;

    @Embedded
    @Column(nullable = false)
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Embedded
    @Column
    private Address address;

    @Embedded
    @Column(name = "phone_number")
    private PhoneNumber phoneNumber;

    @Transient
    private Integer age;

    @Column(name = "clinic_id", nullable = false)
    private Long clinicId;

    @Column(name = "professional_license_number", unique = true, length = 10)
    @Pattern(regexp = "^(CMP)-[0-9]{4,6}$", message = "El número de colegiatura debe tener entre 4 y 6 dígitos")
    private String professionalLicenseNumber;

    @Column(name = "specialty_registration_number", length = 15)
    @Pattern(regexp = "^(ESP|RENES)-[0-9]{3,6}$", message = "El número de registro de especialidad debe seguir el formato ESP-000 o RENES-0000")
    private String specialtyRegistrationNumber;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "shift_name")
    private String shiftName;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public Odontologist() {}

    public Integer getAge() {
        return (birthDate != null) ? birthDate.calculateAge() : null;
    }

    public Odontologist(Long userId, String firstName, String lastName, String birthDate, String email, Long  clinicId) {
        this.userId = userId;
        this.personName = new PersonName(firstName, lastName);
        this.birthDate = new BirthDate(birthDate);
        this.email = new Email(email);
        this.clinicId = clinicId;
    }

    public void updateBasicInfo(String firstName, String lastName) {
        this.personName = new PersonName(firstName, lastName);
    }

    public void updateAdditionalInformation(
            Gender gender,
            Address address,
            String phoneNumber,
            String professionalLicenseNumber,
            String specialtyRegistrationNumber,
            String specialty,
            Long serviceId,
            String shiftName,
            boolean isActive
    ) {
        if (gender != null) this.gender = gender;

        if (address != null) this.address = address;

        this.phoneNumber = (phoneNumber != null && !phoneNumber.isBlank())
                ? new PhoneNumber(phoneNumber)
                : this.phoneNumber;

        if (professionalLicenseNumber != null && !professionalLicenseNumber.isBlank()) {
            this.professionalLicenseNumber = professionalLicenseNumber;
        }

        if (specialtyRegistrationNumber != null && !specialtyRegistrationNumber.isBlank()) {
            this.specialtyRegistrationNumber = specialtyRegistrationNumber;
        }

        if (specialty != null && !specialty.isBlank()) {
            this.specialty = specialty;
        }

        if (serviceId != null) {
            this.serviceId = serviceId;
        }

        if (shiftName != null && !shiftName.isBlank()) {
            this.shiftName = shiftName;
        }

        this.isActive = isActive;
    }
}