package com.sportcalendar.event.domain;

import com.sportcalendar.competition.domain.StageRepository;
import com.sportcalendar.team.domain.TeamRepository;
import com.sportcalendar.venue.domain.VenueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {
    @Bean
    public EventService eventService(
            EventRepository eventRepository,
            TeamRepository teamRepository,
            StageRepository stageRepository,
            VenueRepository venueRepository,
            EventMapper eventMapper) {

        return new EventServiceImpl(
                eventRepository,
                teamRepository,
                stageRepository,
                venueRepository,
                eventMapper
        );
    }

    @Bean
    public EventFacade eventFacade(EventService eventService) {
        return new EventFacade(eventService);
    }
}
