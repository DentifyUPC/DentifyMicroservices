package com.upc.dentify.appointmentservice.application.internal.outboundservices;

import com.upc.dentify.appointmentservice.interfaces.rest.dtos.ServiceExternalResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ExternalServiceService {
    private final RestTemplate restTemplate;
    private final String servicesBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalServiceService(RestTemplate restTemplate,
                                  @Value("${external.services.url}") String servicesBaseUrl) {
        this.restTemplate = restTemplate;
        this.servicesBaseUrl = servicesBaseUrl;
    }

    public ServiceExternalResource getServiceById(Long serviceId) {
        String url = servicesBaseUrl + "/api/v1/acl/services/" + serviceId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ServiceExternalResource> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                ServiceExternalResource.class
        );
        return Objects.requireNonNull(response.getBody());
    }
}
