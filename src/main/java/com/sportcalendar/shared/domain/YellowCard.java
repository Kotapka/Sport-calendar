package com.sportcalendar.shared.domain;

import com.sportcalendar.event.domain.Result;
import com.sportcalendar.player.domain.Player;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "yellow_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YellowCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer yellowCardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_result_id", nullable = false)
    private Result result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private Integer minute;

    private Boolean secondYellow = false;
}
