package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.domain.model.valueobjects.*;
import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

@Entity
public class Patient extends AuditableAbstractAggregateRoot<Patient> {
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

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Transient
    private Integer age;

    @Column(name = "clinic_id", nullable = false)
    private Long clinicId;

    public Patient() {}

    public Patient(Long patientId) {
        this.setId(patientId);
    }

    public Integer getAge() {
        if (birthDate != null) {
            return birthDate.calculateAge();
        }
        return null;
    }

    public Patient(Long userId, String firstName, String lastName, String birthDate, String email, Long  clinicId) {
        this.userId = userId;
        this.personName = new PersonName(firstName, lastName);
        this.birthDate = new BirthDate(birthDate);
        this.email = new Email(email);
        this.clinicId = clinicId;
    }

    public void updateAdditionalInfo(Gender gender, Address address, String phoneNumber) {
        if (gender != null) this.gender = gender;
        if (address != null) this.address = address;
        if (phoneNumber != null && !phoneNumber.isBlank()) this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public void updateBasicInfo(String firstName, String lastName) {
        this.personName = new PersonName(firstName, lastName);
    }

    public PersonName getPersonName() {
        return personName;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    public Email getEmail() {
        return email;
    }

    public Gender getGender() {
        return gender;
    }

    public Address getAddress() {
        return address;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }
}
