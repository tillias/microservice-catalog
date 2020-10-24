package com.github.microcatalog.repository;

import com.github.microcatalog.domain.ReleaseStep;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ReleaseStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleaseStepRepository extends JpaRepository<ReleaseStep, Long> {
}
