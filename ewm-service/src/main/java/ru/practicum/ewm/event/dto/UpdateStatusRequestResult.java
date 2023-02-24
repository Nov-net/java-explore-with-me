package ru.practicum.ewm.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.request.dto.RequestDto;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStatusRequestResult {
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}
