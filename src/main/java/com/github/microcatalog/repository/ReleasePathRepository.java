package com.github.microcatalog.repository;

import com.github.microcatalog.domain.ReleasePath;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ReleasePath entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleasePathRepository extends JpaRepository<ReleasePath, Long> {
}
