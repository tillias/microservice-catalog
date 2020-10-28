package com.github.microcatalog.web.rest.errors.custom;

import com.github.microcatalog.service.custom.exceptions.CircularDependenciesException;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
import com.github.microcatalog.utils.MicroserviceBuilder;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ReleasePathAdviceTraitTest {

    private final ReleasePathAdviceTrait cut = spy(ReleasePathAdviceTrait.class);

    @Mock
    private NativeWebRequest webRequest;

    @Test
    void handleSelfCircularException() {

        final SelfCircularException exception =
            new SelfCircularException("Test message", new MicroserviceBuilder().withId(1L).build());
        ResponseEntity<Problem> response = cut.handleSelfCircularException(exception, webRequest);

        assertThat(response).isNotNull()
            .extracting(HttpEntity::getBody).isNotNull();

        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        assertThat(response.getBody()).isNotNull()
            .extracting(Problem::getParameters, InstanceOfAssertFactories.map(String.class, Object.class))
            .contains(
                entry(ReleasePathAdviceTrait.HEADER_KEY, exception.getClass().getName()),
                entry(ReleasePathAdviceTrait.MESSAGE_KEY, "Test message"),
                entry(ReleasePathAdviceTrait.PAYLOAD_KEY, "id = 1, name = null")
            );
    }

    @Test
    void handleCircularDependenciesException() {
        final CircularDependenciesException exception =
            new CircularDependenciesException("Test message", new HashSet<>(Arrays.asList(
                new MicroserviceBuilder().withId(1L).build(),
                new MicroserviceBuilder().withId(2L).build(),
                new MicroserviceBuilder().withId(3L).build()
            )));

        ResponseEntity<Problem> response = cut.handleCircularDependenciesException(exception, webRequest);

        assertThat(response).isNotNull()
            .extracting(HttpEntity::getBody).isNotNull();

        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        assertThat(response.getBody()).isNotNull()
            .extracting(Problem::getParameters, InstanceOfAssertFactories.map(String.class, Object.class))
            .contains(
                entry(ReleasePathAdviceTrait.HEADER_KEY, exception.getClass().getName()),
                entry(ReleasePathAdviceTrait.MESSAGE_KEY, "Test message"),
                entry(ReleasePathAdviceTrait.PAYLOAD_KEY, "1,2,3")
            );
    }
}
