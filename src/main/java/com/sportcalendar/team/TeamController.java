package com.sportcalendar.team;


import com.sportcalendar.team.domain.TeamService;
import com.sportcalendar.team.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamDto>> searchTeams(@RequestParam String name) {
        return ResponseEntity.ok(teamService.searchTeamsByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getTeamById(@PathVariable Integer id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
        return ResponseEntity.status(201).body(teamService.createTeam(teamDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> updateTeam(@PathVariable Integer id, @RequestBody TeamDto teamDto) {
        return ResponseEntity.ok(teamService.updateTeam(id, teamDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
