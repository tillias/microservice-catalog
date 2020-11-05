package com.github.microcatalog.service.mapper;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MicroserviceMapper {
    MicroserviceDto microserviceToDto(Microservice microservice);
}
