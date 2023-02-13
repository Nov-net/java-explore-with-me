package ru.practicum.ewm.exception;

public class UnknownState extends RuntimeException {
    public UnknownState(String message) {
        super(message);
    }
}
