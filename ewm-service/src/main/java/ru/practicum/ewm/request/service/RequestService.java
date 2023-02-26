package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto saveRequest(Long userId, Long eventId);

    RequestDto updateRequest(Long userId, Long requestId);

    List<RequestDto> getRequest(Long userId);

}
