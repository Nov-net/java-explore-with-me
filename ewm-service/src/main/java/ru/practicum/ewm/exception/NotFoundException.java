package ru.practicum.ewm.exception;

import ru.practicum.ewm.exception.error.ApiError;

public class NotFoundException extends ApiError {
    public NotFoundException(String status, String reason, String message) {
        super(status, reason, message);
    }
}
