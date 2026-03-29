package com.sportcalendar.competition;

import com.sportcalendar.sport.domain.Sport;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer competitionId;

    @Column(nullable = false, unique = true)
    private String originCompetitionId;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_sport_id", nullable = false)
    private Sport sport;
}
