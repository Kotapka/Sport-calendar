package com.sportcalendar.competition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageDto {
    private Integer stageId;
    private String name;
    private Integer ordering;
    private Integer competitionId;
    private String competitionName;
}
