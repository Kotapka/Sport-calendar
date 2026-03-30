package com.sportcalendar.event.domain;

import com.sportcalendar.shared.domain.Goal;
import com.sportcalendar.shared.domain.RedCard;
import com.sportcalendar.shared.domain.YellowCard;
import com.sportcalendar.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    @Builder.Default
    private List<YellowCard> yellowCards = new ArrayList<>();

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    @Builder.Default
    private List<RedCard> redCards = new ArrayList<>();

    public void updateScore(Integer home, Integer away) {
        this.homeGoals = home != null ? home : 0;
        this.awayGoals = away != null ? away : 0;

        if (this.homeGoals > this.awayGoals) {
            this.winnerTeam = (event != null) ? event.getHomeTeam() : null;
        } else if (this.awayGoals > this.homeGoals) {
            this.winnerTeam = (event != null) ? event.getAwayTeam() : null;
        } else {
            this.winnerTeam = null;
        }
    }
}
