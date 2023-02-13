package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.StatsDto;

import java.util.List;

public interface StatsService {

    HitDto saveHit(HitDto hitDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique);

}
