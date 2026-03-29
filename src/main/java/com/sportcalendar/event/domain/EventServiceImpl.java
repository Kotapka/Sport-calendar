package com.sportcalendar.event.domain;

import com.sportcalendar.event.dto.*;
import com.sportcalendar.event.exception.EventNotFoundException;
import com.sportcalendar.event.exception.InvalidEventException;
import com.sportcalendar.competition.domain.StageRepository;
import com.sportcalendar.team.domain.TeamRepository;
import com.sportcalendar.venue.domain.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return eventRepository.findAllWithBasicInfo().stream()
                .map(mapper::toEventResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventDetailResponseDto getEventById(Integer eventId) {
        return eventRepository.findByIdWithDetails(eventId)
                .map(mapper::toEventDetailResponse)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
    }

    @Override
    @Transactional
    public Integer createEvent(CreateEventRequestDto request) {
        Event event = mapper.toEntity(request);

        event.setHomeTeam(teamRepository.findById(request.getHomeTeamId())
                .orElseThrow(() -> new InvalidEventException("Home team not found: " + request.getHomeTeamId())));

        event.setAwayTeam(teamRepository.findById(request.getAwayTeamId())
                .orElseThrow(() -> new InvalidEventException("Away team not found: " + request.getAwayTeamId())));

        event.setStage(stageRepository.findById(request.getStageId())
                .orElseThrow(() -> new InvalidEventException("Stage not found: " + request.getStageId())));

        if (request.getVenueId() != null) {
            event.setVenue(venueRepository.findById(request.getVenueId()).orElse(null));
        }

        Event saved = eventRepository.save(event);
        return saved.getEventId();
    }
}
