package com.sportcalendar.event.dto;


import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CreateEventRequestDto {

    private final Integer season;
    private final String status;
    private final LocalDate dateVenue;
    private final LocalTime timeVenueUtc;
    private final Integer homeTeamId;
    private final Integer awayTeamId;
    private final Integer stageId;
    private final Integer venueId;
    private final String groupName;
}
