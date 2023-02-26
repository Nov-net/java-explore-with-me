package ru.practicum.ewm.exception;

import ru.practicum.ewm.exception.error.ApiError;

public class ForbiddenException extends ApiError {

    public ForbiddenException(String status, String reason, String message) {
        super(status, reason, message);
    }
}
