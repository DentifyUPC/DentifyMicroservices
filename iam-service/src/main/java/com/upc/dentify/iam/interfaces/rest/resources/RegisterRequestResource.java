package com.upc.dentify.iam.interfaces.rest.resources;

public record RegisterRequestResource(String firstName,
                                      String lastName,
                                      String username,
                                      String password,
                                      String birthDate,
                                      String email,
                                      Long identificationTypeId,
                                      Long roleId,
                                      Long clinicId) {
}
