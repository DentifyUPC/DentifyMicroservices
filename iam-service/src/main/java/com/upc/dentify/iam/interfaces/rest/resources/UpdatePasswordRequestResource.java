package com.upc.dentify.iam.interfaces.rest.resources;

public record UpdatePasswordRequestResource(String newPassword,
                                            String confirmNewPassword,
                                            String oldPassword) {
}
