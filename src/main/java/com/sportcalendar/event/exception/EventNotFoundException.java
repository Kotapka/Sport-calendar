package com.sportcalendar.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException {
    public String getMessage() {
        return super.getMessage();
    }
    public EventNotFoundException(String message) {
        super(message);
    }
}
