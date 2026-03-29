package com.sportcalendar.event.domain;

import com.sportcalendar.competition.domain.Competition;
import com.sportcalendar.competition.domain.Stage;
import com.sportcalendar.competition.domain.StageRepository;
import com.sportcalendar.event.dto.CreateEventRequestDto;
import com.sportcalendar.event.dto.EventDetailResponseDto;
import com.sportcalendar.event.dto.EventResponseDto;
import com.sportcalendar.event.exception.EventNotFoundException;
import com.sportcalendar.player.domain.Player;
import com.sportcalendar.shared.domain.Goal;
import com.sportcalendar.sport.domain.Sport;
import com.sportcalendar.team.domain.Team;
import com.sportcalendar.team.domain.TeamRepository;
import com.sportcalendar.venue.domain.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class EventTest {

    private EventServiceImpl eventService;

    @Mock private EventRepository eventRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private StageRepository stageRepository;
    @Mock private VenueRepository venueRepository;

    private final EventMapper mapper = new EventMapper();

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
    void shouldMapCreateRequestToEntity() {
        // given
        CreateEventRequestDto request = CreateEventRequestDto.builder()
                .season(2026)
                .status("scheduled")
                .dateVenue(LocalDate.of(2026, 3, 29))
                .timeVenueUtc(LocalTime.of(21, 0))
                .groupName("Group A")
                .build();

        // when
        Event event = mapper.toEntity(request);

        // then
        assertThat(event).isNotNull();
        assertThat(event.getSeason()).isEqualTo(2026);
        assertThat(event.getStatus()).isEqualTo("scheduled");
        assertThat(event.getDateVenue()).isEqualTo(LocalDate.of(2026, 3, 29));
        assertThat(event.getTimeVenueUtc()).isEqualTo(LocalTime.of(21, 0));
        assertThat(event.getGroupName()).isEqualTo("Group A");
    }

    @Test
    void shouldMapEventDetailWithGoals() {
        // given
        Player player = Player.builder().name("Robert Lewandowski").build();
        Goal goal = Goal.builder().player(player).minute(10).type("NORMAL").build();
        Result result = Result.builder().homeGoals(1).awayGoals(0).goals(List.of(goal)).build();
        Event event = Event.builder().result(result).homeTeam(Team.builder().name("Barcelona").build()).awayTeam(Team.builder().name("Real").build()).stage(new Stage()).build();

        // when
        EventDetailResponseDto dto = mapper.toEventDetailResponse(event);

        // then
        assertThat(dto.getHomeGoals()).isEqualTo(1);
        assertThat(dto.getGoals()).hasSize(1);
        assertThat(dto.getGoals().get(0).getPlayerName()).isEqualTo("Robert Lewandowski");
    }

    @Test
    void shouldHandleNullResultWhenMappingToDetail() {
        // given
        Event event = Event.builder().result(null).homeTeam(Team.builder().name("A").build()).awayTeam(Team.builder().name("B").build()).stage(new Stage()).build();

        // when
        EventDetailResponseDto dto = mapper.toEventDetailResponse(event);

        // then
        assertThat(dto.getHomeGoals()).isZero();
        assertThat(dto.getGoals()).isEmpty();
        assertThat(dto.getWinnerTeamName()).isEqualTo("DRAW");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenEventDoesNotExist() {
        // given
        Integer nonExistingId = 123;
        when(eventRepository.findByIdWithDetails(nonExistingId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EventNotFoundException.class, () -> eventService.getEventById(nonExistingId));
    }

    @Test
    void shouldReturnListOfEvents() {
        // given
        Sport sport = Sport.builder().name("Football").build();
        Competition comp = Competition.builder().name("La Liga").sport(sport).build();
        Stage stage = Stage.builder().name("Regular").competition(comp).build();
        Team home = Team.builder().name("Real Madrid").build();
        Team away = Team.builder().name("FC Barcelona").build();

        Event event1 = Event.builder()
                .eventId(1)
                .season(2026)
                .status("SCHEDULED")
                .homeTeam(home)
                .awayTeam(away)
                .stage(stage)
                .build();

        when(eventRepository.findAllWithBasicInfo()).thenReturn(List.of(event1));

        // when
        List<EventResponseDto> result = eventService.getAllEvents();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isNotNull(); // Teraz Mapper powinien zwrócić pełny obiekt
        assertThat(result.get(0).getHomeTeamName()).isEqualTo("Real Madrid");
    }
}