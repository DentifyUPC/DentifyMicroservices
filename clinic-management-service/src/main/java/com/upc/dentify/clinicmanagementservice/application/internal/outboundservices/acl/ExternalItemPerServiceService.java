package com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl;

import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ItemRequiredResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ExternalItemPerServiceService {
    private final RestTemplate restTemplate;
    private final String serviceCatalogBaseUrl;

    public ExternalItemPerServiceService(RestTemplate restTemplate,
                                         @Value("${external.servicecatalog.url}") String serviceCatalogBaseUrl) {
        this.restTemplate = restTemplate;
        this.serviceCatalogBaseUrl = serviceCatalogBaseUrl;
    }

    public List<ItemRequiredResource> getItemsIdsByServiceId(Long serviceId) {
        String url = serviceCatalogBaseUrl + "/api/v1/acl/items-per-service/" + serviceId;
        ItemRequiredResource[] response = restTemplate.getForObject(url, ItemRequiredResource[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }
}
