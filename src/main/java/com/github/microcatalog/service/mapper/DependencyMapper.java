package com.github.microcatalog.service.mapper;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.service.dto.custom.DependencyDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DependencyMapper {
    DependencyDto dependencyToDto(Dependency dependency);
}
