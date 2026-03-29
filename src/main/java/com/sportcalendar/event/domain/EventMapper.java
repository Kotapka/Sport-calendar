package com.sportcalendar.event.domain;

import com.sportcalendar.event.dto.*;
import com.sportcalendar.shared.domain.Goal;
import com.sportcalendar.shared.domain.RedCard;
import com.sportcalendar.shared.domain.YellowCard;
import com.sportcalendar.shared.dto.GoalDto;
import com.sportcalendar.shared.dto.RedCardDto;
import com.sportcalendar.shared.dto.YellowCardDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventResponseDto toEventResponse(Event event) {
        if (event == null) return null;
        return EventResponseDto.builder()
                .eventId(event.getEventId())
                .season(event.getSeason())
                .status(event.getStatus())
                .dateVenue(event.getDateVenue())
                .timeVenueUtc(event.getTimeVenueUtc())
                .homeTeamName(event.getHomeTeam().getName())
                .awayTeamName(event.getAwayTeam().getName())
                .stageName(event.getStage().getName())
                .competitionName(event.getStage().getCompetition().getName())
                .sportName(event.getStage().getCompetition().getSport().getName())
                .homeGoals(event.getResult() != null ? event.getResult().getHomeGoals() : 0)
                .awayGoals(event.getResult() != null ? event.getResult().getAwayGoals() : 0)
                .build();
    }

    public EventDetailResponseDto toEventDetailResponse(Event event) {
        if (event == null) return null;
        Result res = event.getResult();
        return EventDetailResponseDto.builder()
                .eventId(event.getEventId())
                .season(event.getSeason())
                .status(event.getStatus())
                .dateVenue(event.getDateVenue())
                .timeVenueUtc(event.getTimeVenueUtc())
                .homeTeamName(event.getHomeTeam().getName())
                .awayTeamName(event.getAwayTeam().getName())
                .stageName(event.getStage().getName())
                .groupName(event.getGroupName())
                .homeGoals(res != null ? res.getHomeGoals() : 0)
                .awayGoals(res != null ? res.getAwayGoals() : 0)
                .winnerTeamName(res != null && res.getWinnerTeam() != null ? res.getWinnerTeam().getName() : "DRAW")
                .goals(res != null ? mapGoals(res.getGoals()) : Collections.emptyList())
                .yellowCards(res != null ? mapYellowCards(res.getYellowCards()) : Collections.emptyList())
                .redCards(res != null ? mapRedCards(res.getRedCards()) : Collections.emptyList())
                .build();
    }

    public Event toEntity(CreateEventRequestDto request) {
        if (request == null) return null;
        return Event.builder()
                .season(request.getSeason())
                .status(request.getStatus())
                .dateVenue(request.getDateVenue())
                .timeVenueUtc(request.getTimeVenueUtc())
                .groupName(request.getGroupName())
                .build();
    }

    private List<GoalDto> mapGoals(List<Goal> goals) {
        return goals == null ? Collections.emptyList() : goals.stream()
                .map(g -> GoalDto.builder().playerName(g.getPlayer().getName()).minute(g.getMinute()).type(g.getType()).build())
                .toList();
    }

    private List<YellowCardDto> mapYellowCards(List<YellowCard> cards) {
        return cards == null ? Collections.emptyList() : cards.stream()
                .map(c -> YellowCardDto.builder().playerName(c.getPlayer().getName()).minute(c.getMinute()).secondYellow(c.getSecondYellow()).build())
                .toList();
    }

    private List<RedCardDto> mapRedCards(List<RedCard> cards) {
        return cards == null ? Collections.emptyList() : cards.stream()
                .map(c -> RedCardDto.builder().playerName(c.getPlayer().getName()).minute(c.getMinute()).directRed(c.getDirectRed()).build())
                .toList();
    }
}
