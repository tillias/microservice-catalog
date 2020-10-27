package com.github.microcatalog.web.rest.errors.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.service.custom.exceptions.CircularDependenciesException;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
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

    String HEADER_KEY = "BusinessException";
    String PAYLOAD_KEY = "BusinessPayload";
    String MESSAGE_KEY = "BusinessMessage";

    @ExceptionHandler
    default ResponseEntity<Problem> handleSelfCircularException(final SelfCircularException exception, final NativeWebRequest request) {
        final Microservice source = exception.getSource();
        final Problem problem = Problem.builder()
            .withStatus(UNPROCESSABLE_ENTITY)
            .with(HEADER_KEY, SelfCircularException.class.getName())
            .with(PAYLOAD_KEY, String.format("id = %s, name = %s", source.getId(), source.getName()))
            .with(MESSAGE_KEY, exception.getMessage())
            .build();

        return create(exception, problem, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleCircularDependenciesException(final CircularDependenciesException exception, final NativeWebRequest request) {
        final List<String> microserviceIds = exception.getCycles().stream().map(m -> String.valueOf(m.getId())).collect(Collectors.toList());

        final Problem problem = Problem.builder()
            .withStatus(UNPROCESSABLE_ENTITY)
            .with(HEADER_KEY, CircularDependenciesException.class.getName())
            .with(PAYLOAD_KEY, String.join(",", microserviceIds))
            .with(MESSAGE_KEY, exception.getMessage())
            .build();

        return create(exception, problem, request);
    }
}
