package com.sportcalendar.competition.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stageId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_competition_id", nullable = false)
    private Competition competition;
}
