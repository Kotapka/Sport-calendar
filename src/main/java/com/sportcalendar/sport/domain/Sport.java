package com.sportcalendar.sport.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sportId;

    @Column(nullable = false, length = 50)
    private String name;
}