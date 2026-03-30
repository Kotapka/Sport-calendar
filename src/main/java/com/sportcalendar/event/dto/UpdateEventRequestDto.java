package com.sportcalendar.event.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequestDto {
    private Integer eventId;
    private Integer season;
    private String status;
    private LocalDate dateVenue;
    private LocalTime timeVenueUtc;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private Integer stageId;
    private Integer venueId;
    private String groupName;
    private Integer homeGoals;
    private Integer awayGoals;
}