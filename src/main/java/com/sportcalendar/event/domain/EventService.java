package com.sportcalendar.event.domain;

import com.sportcalendar.event.dto.CreateEventRequestDto;
import com.sportcalendar.event.dto.EventDetailResponseDto;
import com.sportcalendar.event.dto.EventResponseDto;
import com.sportcalendar.event.dto.UpdateEventRequestDto;

import java.util.List;

public interface EventService {
    List<EventResponseDto> getAllEvents();
    EventDetailResponseDto getEventById(Integer eventId);
    Integer createEvent(CreateEventRequestDto request);
    void updateEvent(Integer eventId, UpdateEventRequestDto request);
    void deleteEvent(Integer eventId);
}