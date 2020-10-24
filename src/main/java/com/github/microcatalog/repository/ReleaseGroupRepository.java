package com.github.microcatalog.repository;

import com.github.microcatalog.domain.ReleaseGroup;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ReleaseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleaseGroupRepository extends JpaRepository<ReleaseGroup, Long> {
}
