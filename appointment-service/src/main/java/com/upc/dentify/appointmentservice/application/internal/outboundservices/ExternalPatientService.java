package com.upc.dentify.appointmentservice.application.internal.outboundservices;

import com.upc.dentify.appointmentservice.interfaces.rest.dtos.PatientExternalResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class ExternalPatientService {
    private final RestTemplate restTemplate;
    private final String patientAttentionBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalPatientService(RestTemplate restTemplate,
                                  @Value("${external.patientattention.url}") String patientAttentionBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.patientAttentionBaseUrl = patientAttentionBaseUrl;
    }

    public List<PatientExternalResource> getPatientsByClinicId(Long clinicId) {
        String url = patientAttentionBaseUrl + "/api/v1/acl/patient/clinic/" + clinicId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<PatientExternalResource[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                PatientExternalResource[].class
        );
        return List.of(Objects.requireNonNull(response.getBody()));
    }

    public PatientExternalResource getPatientById(Long patientId) {
        String url = patientAttentionBaseUrl + "/api/v1/acl/patient/" + patientId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<PatientExternalResource> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                PatientExternalResource.class
        );
        return Objects.requireNonNull(response.getBody());
    }
}
