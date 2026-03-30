package com.sportcalendar.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("""
        SELECT e FROM Event e
        JOIN FETCH e.homeTeam
        JOIN FETCH e.awayTeam
        JOIN FETCH e.stage s
        JOIN FETCH s.competition c
        JOIN FETCH c.sport
        LEFT JOIN FETCH e.result
        ORDER BY e.dateVenue ASC, e.timeVenueUtc ASC
    """)
    List<Event> findAllWithBasicInfo();

    @Query("""
    SELECT e FROM Event e
    JOIN FETCH e.homeTeam
    JOIN FETCH e.awayTeam
    JOIN FETCH e.stage s
    LEFT JOIN FETCH e.result r
    WHERE e.eventId = :id
""")
    Optional<Event> findByIdWithDetails(@Param("id") Integer id);

    @Query("""
    SELECT e FROM Event e 
    JOIN FETCH e.homeTeam 
    JOIN FETCH e.awayTeam 
    JOIN FETCH e.stage s 
    JOIN FETCH s.competition c 
    JOIN FETCH c.sport sp
    WHERE (:sportId IS NULL OR sp.sportId = :sportId)
    AND (:date IS NULL OR e.dateVenue = :date)
""")
    List<Event> findByFilters(@Param("sportId") Integer sportId, @Param("date") LocalDate date);
}
