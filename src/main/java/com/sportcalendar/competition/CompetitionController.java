package com.sportcalendar.competition;

import com.sportcalendar.competition.domain.StageService;
import com.sportcalendar.competition.dto.StageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class CompetitionController {

    private final StageService stageService;

    @GetMapping
    public ResponseEntity<List<StageDto>> getAllStages() {
        return ResponseEntity.ok(stageService.getAllStages());
    }

    @GetMapping("/search")
    public ResponseEntity<List<StageDto>> searchStages(@RequestParam String name) {
        return ResponseEntity.ok(stageService.searchStagesByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StageDto> getStageById(@PathVariable Integer id) {
        return ResponseEntity.ok(stageService.getStageById(id));
    }

    @PostMapping
    public ResponseEntity<StageDto> createStage(@RequestBody StageDto stageDto) {
        return ResponseEntity.status(201).body(stageService.createStage(stageDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StageDto> updateStage(@PathVariable Integer id, @RequestBody StageDto stageDto) {
        return ResponseEntity.ok(stageService.updateStage(id, stageDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStage(@PathVariable Integer id) {
        stageService.deleteStage(id);
        return ResponseEntity.noContent().build();
    }
}