package com.sportcalendar.event.domain;

import com.sportcalendar.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resultId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_event_id", nullable = false, unique = true)
    private Event event;

    @Column(nullable = false)
    private Integer homeGoals = 0;

    @Column(nullable = false)
    private Integer awayGoals = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_winner_team_id")
    private Team winnerTeam;

    private String message;
}
