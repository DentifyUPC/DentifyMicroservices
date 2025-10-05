package com.upc.dentify.iam.interfaces.rest.resources;

public record AuthResponseResource(String accessToken,
                                   String refreshToken,
                                   Long userId,
                                   String username,
                                   Long roleId,
                                   Long clinicId) {
}
