package com.github.microcatalog.web.rest.errors.custom;

import com.github.microcatalog.service.custom.exceptions.CircularDependenciesException;
import com.github.microcatalog.service.custom.exceptions.DuplicateDependencyException;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
import com.github.microcatalog.service.dto.custom.DependencyDto;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.AdviceTrait;

import java.util.List;
import java.util.stream.Collectors;

import static org.zalando.problem.Status.UNPROCESSABLE_ENTITY;

/**
 * Using 422 because of https://softwareengineering.stackexchange.com/questions/329229/should-i-return-an-http-400-bad-request-status-if-a-parameter-is-syntactically
 */
public interface ReleasePathAdviceTrait extends AdviceTrait {

    String EXCEPTION_KEY = "BusinessException";
    String MESSAGE_KEY = "BusinessMessage";

    @ExceptionHandler
    default ResponseEntity<Problem> handleDuplicateDependencyException(final DuplicateDependencyException exception, final NativeWebRequest request) {
        final DependencyDto dependency = exception.getDependency();

        final Problem problem = Problem.builder()
            .withStatus(UNPROCESSABLE_ENTITY)
            .with(EXCEPTION_KEY, DuplicateDependencyException.class.getSimpleName())
            .with(MESSAGE_KEY, exception.getMessage())
            .with("dependencyId", String.valueOf(dependency.getId()))
            .with("dependencyName", dependency.getName())
            .build();

        return create(exception, problem, request);

    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleSelfCircularException(final SelfCircularException exception, final NativeWebRequest request) {
        final MicroserviceDto source = exception.getSource();
        final Problem problem = Problem.builder()
            .withStatus(UNPROCESSABLE_ENTITY)
            .with(EXCEPTION_KEY, SelfCircularException.class.getSimpleName())
            .with(MESSAGE_KEY, exception.getMessage())
            .with("microserviceName", source.getName())
            .build();

        return create(exception, problem, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleCircularDependenciesException(final CircularDependenciesException exception, final NativeWebRequest request) {
        final List<String> microservices = exception.getCycles().stream().map(MicroserviceDto::getName).collect(Collectors.toList());

        final Problem problem = Problem.builder()
            .withStatus(UNPROCESSABLE_ENTITY)
            .with(EXCEPTION_KEY, CircularDependenciesException.class.getSimpleName())
            .with(MESSAGE_KEY, exception.getMessage())
            .with("microservices", String.join(",", microservices))
            .build();

        return create(exception, problem, request);
    }
}
