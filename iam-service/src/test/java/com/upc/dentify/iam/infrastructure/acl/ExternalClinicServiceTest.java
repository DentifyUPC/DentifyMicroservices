package com.upc.dentify.iam.infrastructure.acl;

import com.upc.dentify.iam.application.internal.outboundservices.acl.ExternalClinicService;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalClinicServiceTest {

    @Mock RestTemplate restTemplate = mock(RestTemplate.class);

    @Test
    void existsByClinicId_returnsTrueWhenServiceSaysTrue() {
        ExternalClinicService service =
                new ExternalClinicService(restTemplate, "http://clinic");

        String url = "http://clinic/api/v1/acl/clinic/5";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(Boolean.class)))
                .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        boolean result = service.existsByClinicId(5L);

        assertThat(result).isTrue();
    }

    @Test
    void existsByClinicId_returnsFalseWhenNull() {
        ExternalClinicService service =
                new ExternalClinicService(restTemplate, "http://clinic");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Boolean.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        boolean result = service.existsByClinicId(1L);

        assertThat(result).isFalse();
    }
}
