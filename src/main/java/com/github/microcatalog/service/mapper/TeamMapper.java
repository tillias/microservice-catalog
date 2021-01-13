package com.github.microcatalog.service.mapper;

import com.github.microcatalog.domain.Team;
import com.github.microcatalog.service.dto.custom.TeamDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamDto teamToDto(Team team);
}
