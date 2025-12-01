package com.upc.dentify.paymentservice.application.internal.outboundservices;

import com.upc.dentify.paymentservice.interfaces.rest.dtos.AppointmentExternalResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ExternalAppointmentService {
    private final RestTemplate restTemplate;
    private final String appointmentBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalAppointmentService(RestTemplate restTemplate,
                                      @Value("${external.appointment.url}") String appointmentBaseUrl) {
        this.restTemplate = restTemplate;
        this.appointmentBaseUrl = appointmentBaseUrl;
    }

    public AppointmentExternalResource getAppointmentById(Long appointmentId) {
        String url = appointmentBaseUrl + "/api/v1/acl/appointment/" + appointmentId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<AppointmentExternalResource> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    AppointmentExternalResource.class
            );
            return Objects.requireNonNull(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch appointment with id: " + appointmentId, e);
        }
    }

    public boolean existsById(Long appointmentId) {
        try {
            getAppointmentById(appointmentId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
