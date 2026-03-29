package com.sportcalendar.team.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teamId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 150)
    private String officialName;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(length = 10)
    private String abbreviation;

    @Column(length = 3)
    private String teamCountryCode;
}