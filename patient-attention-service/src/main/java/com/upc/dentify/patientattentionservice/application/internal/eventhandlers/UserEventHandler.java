//package com.upc.dentify.patientattentionservice.application.internal.eventhandlers;
//
//import com.upc.dentify.patientattentionservice.domain.model.aggregates.Patient;
//import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PatientRepository;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//@Component("patientUserEventHandler")
//public class UserEventHandler {
//
//    private final PatientRepository patientRepository;
//
//    private static final Long PATIENT_ROLE_ID = 3L;
//
//    public UserEventHandler(PatientRepository patientRepository) {
//        this.patientRepository = patientRepository;
//    }
//
//    @EventListener
//    public void handle(UserCreatedEvent event) {
//        if (PATIENT_ROLE_ID.equals(event.getRole())) {
//            Patient patient = new Patient(
//                    event.getUserId(),
//                    event.getFirstName(),
//                    event.getLastName(),
//                    event.getBirthDate(),
//                    event.getEmail(),
//                    event.getClinicId()
//            );
//            patientRepository.save(patient);
//        }
//    }
//}
