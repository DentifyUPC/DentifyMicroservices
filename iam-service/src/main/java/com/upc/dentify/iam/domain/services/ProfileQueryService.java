package com.upc.dentify.iam.domain.services;

import com.upc.dentify.iam.domain.model.queries.GetUserInformationQuery;
import com.upc.dentify.iam.interfaces.rest.resources.PersonalInformationResponseResource;


public interface ProfileQueryService {
    PersonalInformationResponseResource handle(GetUserInformationQuery query);
}
