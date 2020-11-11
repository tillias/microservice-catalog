package com.github.microcatalog.web.rest.errors.custom;

import com.github.microcatalog.service.custom.exceptions.CircularDependenciesException;
import com.github.microcatalog.service.custom.exceptions.DuplicateDependencyException;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
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

import static com.github.microcatalog.service.dto.custom.builder.DependencyDtoBuilder.aDependencyDto;
import static com.github.microcatalog.service.dto.custom.builder.MicroserviceDtoBuilder.aMicroserviceDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ReleasePathAdviceTraitTest {

    private final ReleasePathAdviceTrait cut = spy(ReleasePathAdviceTrait.class);

    @Mock
    private NativeWebRequest webRequest;

    @Test
    void handleDuplicateDependencyException() {
        final DuplicateDependencyException exception = new DuplicateDependencyException("Test message",
            aDependencyDto()
                .withId(1L)
                .withName("Test Dependency")
                .withSource(aMicroserviceDto().withId(1L).build())
                .withTarget(aMicroserviceDto().withId(2L).build())
                .build());

        ResponseEntity<Problem> response = cut.handleDuplicateDependencyException(exception, webRequest);

        assertThat(response).isNotNull()
            .extracting(HttpEntity::getBody).isNotNull();

        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(response.getBody()).isNotNull()
            .extracting(Problem::getParameters, InstanceOfAssertFactories.map(String.class, Object.class))
            .contains(
                entry(ReleasePathAdviceTrait.EXCEPTION_KEY, exception.getClass().getSimpleName()),
                entry(ReleasePathAdviceTrait.MESSAGE_KEY, "Test message"),
                entry("dependencyId", "1"),
                entry("dependencyName", "Test Dependency")
            );
    }

    @Test
    void handleSelfCircularException() {

        final SelfCircularException exception =
            new SelfCircularException("Test message", aMicroserviceDto().withId(1L).withName("Test").build());
        ResponseEntity<Problem> response = cut.handleSelfCircularException(exception, webRequest);

        assertThat(response).isNotNull()
            .extracting(HttpEntity::getBody).isNotNull();

        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        assertThat(response.getBody()).isNotNull()
            .extracting(Problem::getParameters, InstanceOfAssertFactories.map(String.class, Object.class))
            .contains(
                entry(ReleasePathAdviceTrait.EXCEPTION_KEY, exception.getClass().getSimpleName()),
                entry(ReleasePathAdviceTrait.MESSAGE_KEY, "Test message"),
                entry("microserviceName", "Test")
            );
    }

    @Test
    void handleCircularDependenciesException() {
        final CircularDependenciesException exception =
            new CircularDependenciesException("Test message", new HashSet<>(Arrays.asList(
                aMicroserviceDto().withId(1L).withName("First").build(),
                aMicroserviceDto().withId(2L).withName("Second").build(),
                aMicroserviceDto().withId(3L).withName("Third").build()
            )));

        ResponseEntity<Problem> response = cut.handleCircularDependenciesException(exception, webRequest);

        assertThat(response).isNotNull()
            .extracting(HttpEntity::getBody).isNotNull();

        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        assertThat(response.getBody()).isNotNull()
            .extracting(Problem::getParameters, InstanceOfAssertFactories.map(String.class, Object.class))
            .contains(
                entry(ReleasePathAdviceTrait.EXCEPTION_KEY, exception.getClass().getSimpleName()),
                entry(ReleasePathAdviceTrait.MESSAGE_KEY, "Test message"),
                entry("microservices", "First,Second,Third")
            );
    }
}
