package com.github.microcatalog.service.custom;

import com.github.microcatalog.config.ApplicationProperties;
import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.Status;
import com.github.microcatalog.domain.Team;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.repository.StatusRepository;
import com.github.microcatalog.repository.TeamRepository;
import com.github.microcatalog.service.custom.exceptions.ImportException;
import com.github.microcatalog.service.dto.custom.FullMicroserviceDto;
import com.github.microcatalog.service.dto.custom.MicroserviceImportDescriptorDto;
import com.github.microcatalog.service.mapper.FullMicroserviceMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImportService {

    private final Logger log = LoggerFactory.getLogger(ImportService.class);

    private final ApplicationProperties.Imports.Defaults defaults;
    private final MicroserviceRepository microserviceRepository;
    private final DependencyRepository dependencyRepository;
    private final StatusRepository statusRepository;
    private final TeamRepository teamRepository;

    private final FullMicroserviceMapper microserviceMapper;

    public ImportService(ApplicationProperties applicationProperties,
                         MicroserviceRepository microserviceRepository,
                         DependencyRepository dependencyRepository, StatusRepository statusRepository,
                         TeamRepository teamRepository,
                         FullMicroserviceMapper microserviceMapper) {
        this.defaults = applicationProperties.getImports().getDefaults();
        this.microserviceRepository = microserviceRepository;
        this.dependencyRepository = dependencyRepository;
        this.statusRepository = statusRepository;
        this.teamRepository = teamRepository;
        this.microserviceMapper = microserviceMapper;
    }

    public Optional<FullMicroserviceDto> importFromDescriptor(final MicroserviceImportDescriptorDto descriptorDto) {
        final String name = descriptorDto.getName();
        if (StringUtils.isBlank(name)) {
            throw new ImportException("Microservice name can't be null");
        }


        final Microservice persistent = microserviceRepository.findByName(name);
        if (persistent != null) {
            log.warn("Microservice with name {} already exists. Stopping import", name);
            return Optional.empty();
        } else {
            final Microservice microservice = persistMicroservice(descriptorDto);
            importDependencies(microservice, descriptorDto.getDependencies());

            return Optional.of(microserviceMapper.microserviceToDto(microservice));
        }
    }

    private Microservice persistMicroservice(final MicroserviceImportDescriptorDto descriptorDto) {
        final Microservice microservice = new Microservice();
        microservice.setName(descriptorDto.getName());

        final String description = descriptorDto.getDescription();
        if (StringUtils.isBlank(description)) {
            microservice.setDescription(defaults.getDescription());
        } else {
            microservice.setDescription(description);
        }

        final String imageUrl = descriptorDto.getImageUrl();
        if (StringUtils.isBlank(imageUrl)) {
            microservice.setImageUrl(defaults.getImageUrl());
        } else {
            microservice.setImageUrl(imageUrl);
        }

        final String apiUrl = descriptorDto.getApiUrl();
        if (StringUtils.isBlank(apiUrl)) {
            microservice.setSwaggerUrl(defaults.getApiUrl());
        } else {
            microservice.setSwaggerUrl(apiUrl);
        }

        final String gitUrl = descriptorDto.getGitUrl();
        if (StringUtils.isBlank(gitUrl)) {
            microservice.setGitUrl(defaults.getGitUrl());
        } else {
            microservice.setGitUrl(gitUrl);
        }

        final String ciUrl = descriptorDto.getCiUrl();
        if (StringUtils.isBlank(ciUrl)) {
            microservice.setCiUrl(defaults.getCiUrl());
        } else {
            microservice.setCiUrl(ciUrl);
        }

        String statusName = descriptorDto.getStatus();
        if (StringUtils.isBlank(statusName)) {
            statusName = defaults.getStatus();
        }
        final Status status = getOrCreateStatus(statusName);
        microservice.setStatus(status);

        String teamName = descriptorDto.getTeam();
        if (StringUtils.isBlank(teamName)) {
            teamName = defaults.getTeam().getName();
        }
        final Team team = getOrCreateTeam(teamName);
        microservice.setTeam(team);

        return microserviceRepository.save(microservice);
    }

    private Status getOrCreateStatus(final String name) {
        final Status persistent = statusRepository.findByName(name);
        if (persistent != null) {
            return persistent;
        }

        final Status newStatus = new Status().name(name);
        return statusRepository.save(newStatus);
    }

    private Team getOrCreateTeam(final String name) {
        final Team persistent = teamRepository.findByName(name);
        if (persistent != null) {
            return persistent;
        }

        final Team newTeam = new Team()
            .name(name)
            .teamLead(defaults.getTeam().getTl())
            .productOwner(defaults.getTeam().getPo());
        return teamRepository.save(newTeam);
    }

    private void importDependencies(final Microservice source, final List<MicroserviceImportDescriptorDto> dependencies) {
        for (MicroserviceImportDescriptorDto d : dependencies) {
            Microservice target = microserviceRepository.findByName(d.getName());
            if (target == null) {
                target = persistMicroservice(d);
            }

            final Dependency dependency = new Dependency()
                .name(String.format("%s -> %s", source.getName(), target.getName()))
                .source(source)
                .target(target);

            dependencyRepository.save(dependency);
        }
    }
}
