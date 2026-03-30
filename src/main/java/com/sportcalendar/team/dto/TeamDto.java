package com.sportcalendar.team.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private Integer teamId;
    private String name;
    private String officialName;
    private String slug;
    private String abbreviation;
    private String teamCountryCode;
}
