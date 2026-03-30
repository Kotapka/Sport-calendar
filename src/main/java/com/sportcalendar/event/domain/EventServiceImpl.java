package com.sportcalendar.event.domain;

import com.sportcalendar.event.dto.CreateEventRequestDto;
import com.sportcalendar.event.dto.UpdateEventRequestDto;
import com.sportcalendar.event.dto.EventDetailResponseDto;
import com.sportcalendar.event.dto.EventResponseDto;
import com.sportcalendar.event.exception.EventNotFoundException;
import com.sportcalendar.event.exception.InvalidEventException;
import com.sportcalendar.competition.domain.StageRepository;
import com.sportcalendar.team.domain.TeamRepository;
import com.sportcalendar.venue.domain.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final TeamRepository teamRepository;
    private final StageRepository stageRepository;
    private final VenueRepository venueRepository;
    private final EventMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(mapper::toEventResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventDetailResponseDto getEventById(Integer eventId) {
        return eventRepository.findById(eventId)
                .map(mapper::toEventDetailResponse)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
    }

    @Override
    @Transactional
    public Integer createEvent(CreateEventRequestDto request) {
        validateDateAndStatus(request.getDateVenue(), request.getStatus());
        validateTeams(request.getHomeTeamId(), request.getAwayTeamId());
        Event event = mapper.toEntity(request);

        event.setHomeTeam(teamRepository.findById(request.getHomeTeamId())
                .orElseThrow(() -> new InvalidEventException("Home team not found")));
        event.setAwayTeam(teamRepository.findById(request.getAwayTeamId())
                .orElseThrow(() -> new InvalidEventException("Away team not found")));
        event.setStage(stageRepository.findById(request.getStageId())
                .orElseThrow(() -> new InvalidEventException("Stage not found")));

        if (request.getVenueId() != null) {
            event.setVenue(venueRepository.findById(request.getVenueId()).orElse(null));
        }

        Event saved = eventRepository.save(event);
        return saved.getEventId();
    }

    @Override
    @Transactional
    public void updateEvent(Integer id, UpdateEventRequestDto request) {
        validateDateAndStatus(request.getDateVenue(), request.getStatus());
        validateTeams(request.getHomeTeamId(), request.getAwayTeamId());

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));
        event.setSeason(request.getSeason());
        event.setStatus(request.getStatus());
        event.setDateVenue(request.getDateVenue());
        event.setTimeVenueUtc(request.getTimeVenueUtc());
        event.setGroupName(request.getGroupName());
        event.setHomeTeam(teamRepository.findById(request.getHomeTeamId()).orElseThrow());
        event.setAwayTeam(teamRepository.findById(request.getAwayTeamId()).orElseThrow());
        event.setStage(stageRepository.findById(request.getStageId()).orElseThrow());
        handleResultUpdate(event, request);

        eventRepository.save(event);
    }

    @Override
    @Transactional
    public void deleteEvent(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Event not found with id: " + eventId);
        }
        eventRepository.deleteById(eventId);
    }

    private void handleResultUpdate(Event event, UpdateEventRequestDto request) {
        if ("scheduled".equalsIgnoreCase(request.getStatus())) {
            event.setResult(null);
        } else {
            Result result = event.getResult();
            if (result == null) {
                result = Result.builder()
                        .event(event)
                        .build();
                event.setResult(result);
            }

            result.setHomeGoals(request.getHomeGoals() != null ? request.getHomeGoals() : 0);
            result.setAwayGoals(request.getAwayGoals() != null ? request.getAwayGoals() : 0);

            if (result.getHomeGoals() > result.getAwayGoals()) {
                result.setWinnerTeam(event.getHomeTeam());
            } else if (result.getAwayGoals() > result.getHomeGoals()) {
                result.setWinnerTeam(event.getAwayTeam());
            } else {
                result.setWinnerTeam(null);
            }
        }
    }

    private void validateDateAndStatus(LocalDate eventDate, String status) {
        LocalDate today = LocalDate.now();

        if ("finished".equalsIgnoreCase(status) && eventDate.isAfter(today)) {
            throw new InvalidEventException("Cannot set FINISHED for future event");
        }

        if ("scheduled".equalsIgnoreCase(status) && eventDate.isBefore(today)) {
            throw new InvalidEventException("Cannot set SCHEDULED for past event");
        }
    }

    private void validateTeams(Integer homeId, Integer awayId) {
        if (homeId.equals(awayId)) {
            throw new InvalidEventException("Home and away must be different teams.");
        }
    }
}