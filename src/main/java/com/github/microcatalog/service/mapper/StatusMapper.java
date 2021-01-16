package com.github.microcatalog.service.mapper;

import com.github.microcatalog.domain.Status;
import com.github.microcatalog.service.dto.custom.StatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusDto statusToDto(Status status);
}
