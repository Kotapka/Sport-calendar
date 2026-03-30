package com.sportcalendar.competition.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StageRepository extends JpaRepository<Stage, Integer> {
    List<Stage> findByNameContainingIgnoreCase(String name);
    Optional<Stage> findByName(String name);
}
