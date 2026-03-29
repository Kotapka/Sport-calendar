package com.sportcalendar.shared.domain;

import com.sportcalendar.event.domain.Result;
import com.sportcalendar.player.domain.Player;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_result_id", nullable = false)
    private Result result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private Integer minute;

    @Column(length = 20)
    private String type;

    @Column(length = 50)
    private String description;
}
