package com.sportcalendar.shared.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class GoalDto {
    private final String playerName;
    private final Integer minute;
    private final String type;
}
