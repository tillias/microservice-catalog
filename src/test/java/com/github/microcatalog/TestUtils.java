package com.github.microcatalog;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.service.dto.custom.DependencyDto;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import com.github.microcatalog.utils.MicroserviceBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Test helper
 */
public class TestUtils {

    public static Dependency dependency(final Integer id, final Integer sourceId, final Integer targetId) {
        final Dependency dependency = new Dependency();
        dependency.setName(String.format("%s->%s", sourceId, targetId));

        if (id != null) {
            dependency.setId(Long.valueOf(id));
        }

        if (sourceId != null) {
            dependency.setSource(new MicroserviceBuilder()
                .withId(Long.valueOf(sourceId))
                .withName(String.valueOf(sourceId))
                .build());
        }

        if (targetId != null) {
            dependency.setTarget(new MicroserviceBuilder()
                .withId(Long.valueOf(targetId))
                .withName(String.valueOf(targetId))
                .build());
        }

        return dependency;
    }

    public static Microservice microservice(final Integer id) {
        final Microservice microservice = new Microservice();
        if (id != null) {
            microservice.setId(Long.valueOf(id));
            microservice.setName(String.valueOf(id));
        }

        return microservice;
    }

    public static DependencyDto getById(List<DependencyDto> list, Long dependencyId) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        Optional<DependencyDto> maybeFound = list.stream()
            .filter(m -> Objects.equals(m.getId(), dependencyId)).findFirst();

        return maybeFound.orElse(null);
    }

    public static MicroserviceDto getSource(List<DependencyDto> list, Long dependencyId) {
        DependencyDto dto = getById(list, dependencyId);

        if (dto != null) {
            return dto.getSource();
        }

        return null;
    }

    public static MicroserviceDto getTarget(List<DependencyDto> list, Long dependencyId) {
        DependencyDto dto = getById(list, dependencyId);

        if (dto != null) {
            return dto.getTarget();
        }

        return null;
    }
}
