//package com.upc.dentify.patientattention.application.internal.eventhandlers;
//
//import com.upc.dentify.iam.domain.events.UserUpdatedEvent;
//import com.upc.dentify.patientattention.infrastructure.persistence.jpa.repositories.PatientRepository;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//@Service("patientUserUpdatedEventHandler")
//public class UserUpdatedEventHandler {
//    private final PatientRepository patientRepository;
//
//    public UserUpdatedEventHandler(PatientRepository patientRepository) {
//        this.patientRepository = patientRepository;
//    }
//
//    @EventListener
//    public void handle(UserUpdatedEvent event) {
//        patientRepository.findByUserId(event.userId()).ifPresent(patient -> {
//            patient.updateBasicInfo(
//                    event.firstName(),
//                    event.lastName(),
//                    event.email(),
//                    event.birthdate()
//            );
//            patientRepository.save(patient);
//        });
//    }
//}
