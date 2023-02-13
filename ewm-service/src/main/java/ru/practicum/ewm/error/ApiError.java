package ru.practicum.ewm.error;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {

    List<String> errors;

    String message; // 3

    String reason; // 2

    Enum status; // 1 enum?

    LocalDateTime timestamp; // 4
}
