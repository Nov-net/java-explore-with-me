package ru.practicum.ewm.request.dto;

import ru.practicum.ewm.request.model.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.request.model.Status.PENDING;

public class RequestMapper {

    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .created(request.getCreated().format(pattern))
                .event(request.getEvent())
                .id(request.getId())
                .requester(request.getRequester())
                .status(request.getStatus())
                .build();
    }

    public static List<RequestDto> mapToRequestDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    public static Request toRequest(Long userId, Long eventId) {
        return Request.builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(PENDING)
                .build();
    }

}