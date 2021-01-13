package com.github.microcatalog.repository;

import com.github.microcatalog.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
}
