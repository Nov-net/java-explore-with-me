package ru.practicum.ewm.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.request.model.Status;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {

    String created;
    Long event;
    Long id;
    Long requester;
    Status status;

}
