package com.github.microcatalog.service.custom;

import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import com.github.microcatalog.service.mapper.MicroserviceMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MicroserviceService {

    private final MicroserviceRepository repository;
    private final MicroserviceMapper mapper;

    public MicroserviceService(MicroserviceRepository repository, MicroserviceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<MicroserviceDto> findAllById(final List<Long> ids) {
        return repository.findAllById(ids).stream()
            .map(mapper::microserviceToDto)
            .collect(Collectors.toList());
    }

    public long deleteByName(final String name) {
        return repository.deleteByName(name);
    }
}
