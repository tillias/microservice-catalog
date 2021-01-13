package com.github.microcatalog.repository;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.Status;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Status entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByName(String name);
}
