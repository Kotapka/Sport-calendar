package com.sportcalendar.event;

import com.sportcalendar.event.dto.*;
import com.sportcalendar.event.domain.EventFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class EventController {

    private final EventFacade eventFacade;

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        return ResponseEntity.ok(eventFacade.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponseDto> getEventById(@PathVariable Integer id) {
        return ResponseEntity.ok(eventFacade.getEventById(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createEvent(@RequestBody CreateEventRequestDto request) {
        Integer eventId = eventFacade.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventId);
    }
}