package com.sportcalendar.team.domain;

import com.sportcalendar.team.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public List<TeamDto> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TeamDto> searchTeamsByName(String name) {
        return teamRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TeamDto getTeamById(Integer id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        return mapToDto(team);
    }

    @Transactional
    public TeamDto createTeam(TeamDto teamDto) {
        Team team = Team.builder()
                .name(teamDto.getName())
                .officialName(teamDto.getOfficialName())
                .slug(teamDto.getSlug())
                .abbreviation(teamDto.getAbbreviation())
                .teamCountryCode(teamDto.getTeamCountryCode())
                .build();

        Team savedTeam = teamRepository.save(team);
        return mapToDto(savedTeam);
    }

    @Transactional
    public TeamDto updateTeam(Integer id, TeamDto teamDto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));

        team.setName(teamDto.getName());
        team.setOfficialName(teamDto.getOfficialName());
        team.setSlug(teamDto.getSlug());
        team.setAbbreviation(teamDto.getAbbreviation());
        team.setTeamCountryCode(teamDto.getTeamCountryCode());

        Team updatedTeam = teamRepository.save(team);
        return mapToDto(updatedTeam);
    }

    @Transactional
    public void deleteTeam(Integer id) {
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Team not found with id: " + id);
        }
        teamRepository.deleteById(id);
    }

    private TeamDto mapToDto(Team team) {
        return TeamDto.builder()
                .teamId(team.getTeamId())
                .name(team.getName())
                .officialName(team.getOfficialName())
                .slug(team.getSlug())
                .abbreviation(team.getAbbreviation())
                .teamCountryCode(team.getTeamCountryCode())
                .build();
    }
}