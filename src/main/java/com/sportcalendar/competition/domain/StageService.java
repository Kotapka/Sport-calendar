package com.sportcalendar.competition.domain;

import com.sportcalendar.competition.dto.StageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StageService {

    private final StageRepository stageRepository;
    private final CompetitionRepository competitionRepository;

    public List<StageDto> getAllStages() {
        return stageRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<StageDto> searchStagesByName(String name) {
        return stageRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public StageDto getStageById(Integer id) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stage not found with id: " + id));
        return mapToDto(stage);
    }

    @Transactional
    public StageDto createStage(StageDto stageDto) {
        Stage stage = new Stage();
        stage.setName(stageDto.getName());
        stage.setOrdering(stageDto.getOrdering() != null ? stageDto.getOrdering() : 0);

        if (stageDto.getCompetitionId() != null) {
            Competition competition = competitionRepository.findById(stageDto.getCompetitionId())
                    .orElseThrow(() -> new RuntimeException("Competition not found with id: " + stageDto.getCompetitionId()));
            stage.setCompetition(competition);
        } else {
            throw new RuntimeException("Competition ID is required");
        }

        Stage savedStage = stageRepository.save(stage);
        return mapToDto(savedStage);
    }

    @Transactional
    public StageDto updateStage(Integer id, StageDto stageDto) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stage not found with id: " + id));

        stage.setName(stageDto.getName());
        stage.setOrdering(stageDto.getOrdering());

        if (stageDto.getCompetitionId() != null) {
            Competition competition = competitionRepository.findById(stageDto.getCompetitionId())
                    .orElseThrow(() -> new RuntimeException("Competition not found with id: " + stageDto.getCompetitionId()));
            stage.setCompetition(competition);
        }

        Stage updatedStage = stageRepository.save(stage);
        return mapToDto(updatedStage);
    }

    @Transactional
    public void deleteStage(Integer id) {
        if (!stageRepository.existsById(id)) {
            throw new RuntimeException("Stage not found with id: " + id);
        }
        stageRepository.deleteById(id);
    }

    private StageDto mapToDto(Stage stage) {
        return StageDto.builder()
                .stageId(stage.getStageId())
                .name(stage.getName())
                .ordering(stage.getOrdering())
                .competitionId(stage.getCompetition() != null ? stage.getCompetition().getCompetitionId() : null)
                .competitionName(stage.getCompetition() != null ? stage.getCompetition().getName() : null)
                .build();
    }
}
