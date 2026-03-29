package com.sportcalendar.shared.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RedCardDto {
    private final String playerName;
    private final Integer minute;
    private final boolean directRed;
}