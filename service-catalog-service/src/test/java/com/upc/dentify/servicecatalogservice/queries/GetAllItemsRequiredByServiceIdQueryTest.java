package com.upc.dentify.servicecatalogservice.queries;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsRequiredByServiceIdQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Query Test - GetAllItemsRequiredByServiceIdQuery")
class GetAllItemsRequiredByServiceIdQueryTest {

    private static final String EXPECTED_MSG =
            "Service id cannot  be null or less than zero"; // <-- mensaje EXACTO

    @Test
    void shouldCreateQueryWhenIdIsValid() {
        GetAllItemsRequiredByServiceIdQuery query =
                new GetAllItemsRequiredByServiceIdQuery(10L);

        assertThat(query.serviceId()).isEqualTo(10L);
    }

    @Test
    void shouldFailWhenIdIsNull() {
        assertThatThrownBy(() ->
                new GetAllItemsRequiredByServiceIdQuery(null)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(EXPECTED_MSG);
    }

    @Test
    void shouldFailWhenIdIsZero() {
        assertThatThrownBy(() ->
                new GetAllItemsRequiredByServiceIdQuery(0L)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(EXPECTED_MSG);
    }

    @Test
    void shouldFailWhenIdIsNegative() {
        assertThatThrownBy(() ->
                new GetAllItemsRequiredByServiceIdQuery(-5L)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(EXPECTED_MSG);
    }
}
