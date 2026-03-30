package com.sportcalendar.domain;

import com.sportcalendar.competition.domain.Stage;
import com.sportcalendar.competition.domain.StageRepository;
import com.sportcalendar.event.domain.*;
import com.sportcalendar.event.dto.CreateEventRequestDto;
import com.sportcalendar.event.dto.UpdateEventRequestDto;
import com.sportcalendar.event.dto.EventResponseDto;
import com.sportcalendar.event.exception.EventNotFoundException;
import com.sportcalendar.event.exception.InvalidEventException;
import com.sportcalendar.team.domain.Team;
import com.sportcalendar.team.domain.TeamRepository;
import com.sportcalendar.venue.domain.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventTest {

    private EventServiceImpl eventService;

    @Mock
    private EventRepository eventRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private StageRepository stageRepository;
    @Mock
    private VenueRepository venueRepository;
    @Mock
    private EventMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventServiceImpl(
                eventRepository,
                teamRepository,
                stageRepository,
                venueRepository,
                mapper
        );
    }

    @Test
    @DisplayName("Should return list of events mapped to DTO")
    void shouldReturnListOfEvents() {
        // given
        Event event = Event.builder().eventId(1).build();
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(mapper.toEventResponse(any())).thenReturn(new EventResponseDto());

        // when
        List<EventResponseDto> result = eventService.getAllEvents();

        // then
        assertThat(result).hasSize(1);
        verify(eventRepository).findAll();
    }

    @Test
    @DisplayName("Should throw NotFoundException when event does not exist")
    void shouldThrowNotFoundExceptionWhenEventDoesNotExist() {
        // given
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EventNotFoundException.class, () -> eventService.getEventById(1));
    }


    @Test
    @DisplayName("1. Should create Result entity when status changes from scheduled to finished")
    void shouldCreateResultWhenStatusChangesToFinished() {
        // given
        Integer id = 1;
        UpdateEventRequestDto request = UpdateEventRequestDto.builder()
                .status("finished")
                .dateVenue(LocalDate.now().minusDays(1))
                .homeGoals(3).awayGoals(1)
                .homeTeamId(10).awayTeamId(20).stageId(1)
                .build();

        Event existingEvent = Event.builder().eventId(id).status("scheduled").result(null).build();

        when(eventRepository.findById(id)).thenReturn(Optional.of(existingEvent));
        when(teamRepository.findById(10)).thenReturn(Optional.of(Team.builder().name("Home").build()));
        when(teamRepository.findById(20)).thenReturn(Optional.of(Team.builder().name("Away").build()));
        when(stageRepository.findById(1)).thenReturn(Optional.of(new Stage()));

        // when
        eventService.updateEvent(id, request);

        // then
        assertThat(existingEvent.getResult()).isNotNull();
        assertThat(existingEvent.getResult().getHomeGoals()).isEqualTo(3);
        verify(eventRepository).save(existingEvent);
    }

    @Test
    @DisplayName("2. Should remove Result (orphan removal) when status is set back to scheduled")
    void shouldRemoveResultWhenStatusSetToScheduled() {
        // given
        Integer id = 1;
        Event event = Event.builder()
                .status("finished")
                .result(new Result())
                .build();

        UpdateEventRequestDto request = UpdateEventRequestDto.builder()
                .status("scheduled")
                .dateVenue(LocalDate.now().plusDays(5))
                .homeTeamId(1).awayTeamId(2).stageId(1)
                .build();

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(teamRepository.findById(any())).thenReturn(Optional.of(new Team()));
        when(stageRepository.findById(any())).thenReturn(Optional.of(new Stage()));

        // when
        eventService.updateEvent(id, request);

        // then
        assertThat(event.getResult()).isNull();
    }

    @ParameterizedTest
    @CsvSource({
            "finished, 2099-01-01, Cannot set FINISHED for future event",
            "scheduled, 2020-01-01, Cannot set SCHEDULED for past event"
    })
    @DisplayName("3. Parameterized: Should validate illogical date/status combinations")
    void shouldThrowExceptionForInvalidDateStatus(String status, String date, String msg) {
        // given
        UpdateEventRequestDto request = UpdateEventRequestDto.builder()
                .status(status)
                .dateVenue(LocalDate.parse(date))
                .build();

        // when & then
        InvalidEventException ex = assertThrows(InvalidEventException.class,
                () -> eventService.updateEvent(1, request));
        assertThat(ex.getMessage()).contains(msg);
    }

    @Test
    @DisplayName("4. Should identify winner team correctly (Away wins)")
    void shouldIdentifyWinnerTeamCorrectly() {
        // given
        Team home = Team.builder().name("Home").build();
        Team away = Team.builder().name("Away").build();
        Event event = Event.builder().homeTeam(home).awayTeam(away).build();

        UpdateEventRequestDto request = UpdateEventRequestDto.builder()
                .status("finished")
                .homeGoals(0).awayGoals(2)
                .homeTeamId(1).awayTeamId(2).stageId(1).dateVenue(LocalDate.now())
                .build();

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(teamRepository.findById(1)).thenReturn(Optional.of(home));
        when(teamRepository.findById(2)).thenReturn(Optional.of(away));
        when(stageRepository.findById(any())).thenReturn(Optional.of(new Stage()));

        // when
        eventService.updateEvent(1, request);

        // then
        assertThat(event.getResult().getWinnerTeam()).isEqualTo(away);
    }

    @Test
    @DisplayName("5. Should throw exception when teams are identical")
    void shouldThrowExceptionWhenTeamsAreIdentical() {
        // given
        CreateEventRequestDto request = CreateEventRequestDto.builder()
                .homeTeamId(99).awayTeamId(99)
                .status("scheduled").dateVenue(LocalDate.now().plusDays(1))
                .build();

        // when & then
        InvalidEventException ex = assertThrows(InvalidEventException.class,
                () -> eventService.createEvent(request));
        assertThat(ex.getMessage()).isEqualTo("Home and away must be different teams.");
    }
}