package com.github.microcatalog.repository;

import com.github.microcatalog.domain.Dependency;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Dependency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Long> {
}
