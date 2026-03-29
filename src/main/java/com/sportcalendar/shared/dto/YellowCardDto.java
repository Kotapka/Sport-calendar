package com.sportcalendar.shared.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class YellowCardDto {
    private final String playerName;
    private final Integer minute;
    private final boolean secondYellow;
}
