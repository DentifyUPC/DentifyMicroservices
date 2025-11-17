package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Anamnesis;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateAnamnesisCommand;
import com.upc.dentify.patientattentionservice.domain.model.valueobjects.PhoneNumber;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.AnamnesisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnamnesisCommandServiceImplTest {

    @Mock
    private AnamnesisRepository anamnesisRepository;

    @InjectMocks
    private AnamnesisCommandServiceImpl service;

    @Test
    void handle_updateSuccess_returnsUpdatedAnamnesis() {
        long id = 1L;
        String newClinicalBackground = "No allergies";
        boolean newLowBP = true;
        boolean newHighBP = false;
        boolean newSmoke = true;
        String newCurrentMedications = "aspirin";
        String newEmergencyContact = "923456789";

        UpdateAnamnesisCommand cmd = new UpdateAnamnesisCommand(
                id,
                newClinicalBackground,
                newLowBP,
                newHighBP,
                newSmoke,
                newCurrentMedications,
                newEmergencyContact
        );

        Anamnesis existing = mock(Anamnesis.class);

        when(anamnesisRepository.findById(id)).thenReturn(Optional.of(existing));
        when(anamnesisRepository.save(any(Anamnesis.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Anamnesis> result = service.handle(cmd);

        assertThat(result).isPresent();

        InOrder inOrder = inOrder(existing);
        inOrder.verify(existing).setClinicalBackground(newClinicalBackground);
        inOrder.verify(existing).setLowBloodPressure(newLowBP);
        inOrder.verify(existing).setHighBloodPressure(newHighBP);
        inOrder.verify(existing).setSmoke(newSmoke);
        inOrder.verify(existing).setCurrentMedications(newCurrentMedications);

        ArgumentCaptor<PhoneNumber> phoneCaptor = ArgumentCaptor.forClass(PhoneNumber.class);
        verify(existing).setEmergencyContact(phoneCaptor.capture());
        PhoneNumber passedPhone = phoneCaptor.getValue();
        assertThat(passedPhone).isNotNull();
        assertThat(passedPhone).isEqualTo(new PhoneNumber(newEmergencyContact));

        verify(anamnesisRepository).save(existing);
    }

    @Test
    void handle_notFound_returnsEmptyOptional() {
        long id = 42L;
        UpdateAnamnesisCommand cmd = new UpdateAnamnesisCommand(
                id,
                "bg",
                false,
                false,
                false,
                "none",
                "000"
        );

        when(anamnesisRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Anamnesis> result = service.handle(cmd);

        assertThat(result).isEmpty();
        verify(anamnesisRepository).findById(id);
        verifyNoMoreInteractions(anamnesisRepository);
    }
}
