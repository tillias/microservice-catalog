package com.github.microcatalog.repository;

import com.github.microcatalog.domain.Microservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Microservice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MicroserviceRepository extends JpaRepository<Microservice, Long> {
    Long deleteByName(String name);

    List<Microservice> findByName(String name);
}
