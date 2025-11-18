package com.upc.dentify.patientattentionservice.application.internal.outboundservices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalAppointmentService {
    private final RestTemplate restTemplate;
    private final String appointmentServiceUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalAppointmentService(RestTemplate restTemplate,
                               @Value("${external.appointment.url}") String appointmentServiceUrl) {
        this.restTemplate = restTemplate;
        this.appointmentServiceUrl = appointmentServiceUrl;
    }

    public Boolean existsById(Long id) {
        String url = appointmentServiceUrl + "/api/v1/acl/appointment/" + id + "/exists";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                Boolean.class
        );
        return response.getBody() != null && response.getBody();
    }
}
