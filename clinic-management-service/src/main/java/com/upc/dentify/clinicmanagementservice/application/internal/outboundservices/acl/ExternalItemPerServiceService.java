package com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl;

import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ItemRequiredResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Objects;

@Service
public class ExternalItemPerServiceService {
    private final RestTemplate restTemplate;
    private final String serviceCatalogBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalItemPerServiceService(RestTemplate restTemplate,
                                         @Value("${external.servicecatalog.url}") String serviceCatalogBaseUrl) {
        this.restTemplate = restTemplate;
        this.serviceCatalogBaseUrl = serviceCatalogBaseUrl;
    }

    public List<ItemRequiredResource> getItemsIdsByServiceId(Long serviceId) {
        String url = serviceCatalogBaseUrl + "/api/v1/acl/items-per-service/" + serviceId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ItemRequiredResource[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                ItemRequiredResource[].class
        );
        return List.of(Objects.requireNonNull(response.getBody()));
    }
}