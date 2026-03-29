package com.sportcalendar.event.domain;

import com.sportcalendar.competition.domain.Stage;
import com.sportcalendar.team.domain.Team;
import com.sportcalendar.venue.domain.Venue;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    private Integer season;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "date_venue", nullable = false)
    private LocalDate dateVenue;

    @Column(name = "time_venue_utc", nullable = false)
    private LocalTime timeVenueUtc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_away_team_id", nullable = false)
    private Team awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_stage_id", nullable = false)
    private Stage stage;

    @Column(name = "group_name", length = 50)
    private String groupName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_venue_id")
    private Venue venue;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Result result;
}
