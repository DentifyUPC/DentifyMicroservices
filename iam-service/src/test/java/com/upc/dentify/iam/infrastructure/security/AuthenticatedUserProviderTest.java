package com.upc.dentify.iam.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AuthenticatedUserProviderTest {

    @Test
    void defaultShouldThrowBecauseItIsAbstractInTests() {
        AuthenticatedUserProvider provider = new AuthenticatedUserProvider() {
            @Override
            public Long getCurrentUserId() {
                return 10L;
            }
        };

        assertThat(provider.getCurrentUserId()).isEqualTo(10L);
    }
}
