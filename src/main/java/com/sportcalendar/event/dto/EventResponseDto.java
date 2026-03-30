package com.sportcalendar.event.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class EventResponseDto {
    private final Integer eventId;
    private final Integer season;
    private final String status;
    private final LocalDate dateVenue;
    private final LocalTime timeVenueUtc;
    private final String homeTeamName;
    private final String awayTeamName;
    private final String stageName;
    private final String competitionName;
    private final String sportName;
    private final Integer homeGoals;
    private final Integer awayGoals;
}
