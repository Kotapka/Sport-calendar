package com.sportcalendar.event.domain;

import com.sportcalendar.event.dto.CreateEventRequestDto;
import com.sportcalendar.event.dto.EventDetailResponseDto;
import com.sportcalendar.event.dto.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventFacade {

    private final EventService eventService;

    public List<EventResponseDto> getAllEvents() {
        return eventService.getAllEvents();
    }

    public EventDetailResponseDto getEventById(Integer eventId) {
        return eventService.getEventById(eventId);
    }

    public Integer createEvent(CreateEventRequestDto request) {
        return eventService.createEvent(request);
    }
}
