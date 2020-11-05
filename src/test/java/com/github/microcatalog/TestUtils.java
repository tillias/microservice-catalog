package com.github.microcatalog;

import com.github.microcatalog.service.dto.custom.DependencyDto;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Test helper
 */
public class TestUtils {

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
