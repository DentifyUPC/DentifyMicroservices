package com.upc.dentify.iam.interfaces.rest.resources;

public record PersonalInformationResponseResource(Long userId,
                                                  String fullName,
                                                  String username,
                                                  Long roleId,
                                                  Long clinicId) {
}
