package ru.practicum.ewm.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.request.model.Status;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStatusRequest {
    List<Long> requestIds;
    Status status;
}
