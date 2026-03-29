package com.sportcalendar.event.dto;

import com.sportcalendar.shared.dto.GoalDto;
import com.sportcalendar.shared.dto.RedCardDto;
import com.sportcalendar.shared.dto.YellowCardDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class EventDetailResponseDto {
    private final Integer eventId;
    private final Integer season;
    private final String status;
    private final LocalDate dateVenue;
    private final LocalTime timeVenueUtc;

    private final String homeTeamName;
    private final String awayTeamName;
    private final String stageName;
    private final String groupName;

    private final Integer homeGoals;
    private final Integer awayGoals;
    private final String winnerTeamName;

    private final List<GoalDto> goals;
    private final List<YellowCardDto> yellowCards;
    private final List<RedCardDto> redCards;
}
