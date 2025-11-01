package com.upc.dentify.servicecatalogservice.interfaces.acl;

import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource;
import java.util.List;

public interface ServiceContextFacade {
    List<ServiceResource> getAllServices();
}
