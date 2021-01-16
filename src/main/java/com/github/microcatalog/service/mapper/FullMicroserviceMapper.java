package com.github.microcatalog.service.mapper;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.service.dto.custom.FullMicroserviceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FullMicroserviceMapper {
    FullMicroserviceDto microserviceToDto(Microservice microservice);
}
