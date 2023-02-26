package ru.practicum.ewm.exception;

import ru.practicum.ewm.exception.error.ApiError;

public class UnknownState extends ApiError {
    public UnknownState(String status, String reason, String message) {
        super(status, reason, message);
    }
}
