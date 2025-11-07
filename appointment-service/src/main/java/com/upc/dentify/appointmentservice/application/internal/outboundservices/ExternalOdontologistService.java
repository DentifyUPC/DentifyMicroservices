package com.upc.dentify.appointmentservice.application.internal.outboundservices;

import com.upc.dentify.appointmentservice.interfaces.rest.dtos.OdontologistExternalResource;
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
public class ExternalOdontologistService {
    private final RestTemplate restTemplate;
    private final String practiceManagementBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalOdontologistService(RestTemplate restTemplate,
                                       @Value("${external.practicemanagement.url}") String practiceManagementBaseUrl) {
        this.restTemplate = restTemplate;
        this.practiceManagementBaseUrl = practiceManagementBaseUrl;
    }

    public List<OdontologistExternalResource> getAllOdontologistsByClinicId(Long clinicId) {
        String url = practiceManagementBaseUrl + "/api/v1/acl/odontologist/clinic/" + clinicId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<OdontologistExternalResource[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                OdontologistExternalResource[].class
        );
        return List.of(Objects.requireNonNull(response.getBody()));
    }

    public OdontologistExternalResource getOdontologistById(Long id) {
        String url = practiceManagementBaseUrl + "/api/v1/acl/odontologist/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<OdontologistExternalResource> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                OdontologistExternalResource.class
        );
        return Objects.requireNonNull(response.getBody());
    }
}
