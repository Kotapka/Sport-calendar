package com.sportcalendar.venue.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "venue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer venueId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String city;
}
