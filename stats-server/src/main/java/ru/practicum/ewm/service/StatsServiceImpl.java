package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.HitMapper;
import ru.practicum.ewm.dto.StatsDto;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.ewm.dto.HitMapper.toHit;
import static ru.practicum.ewm.dto.HitMapper.toHitDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public HitDto saveHit(HitDto hitDto) {
        log.info("Запрос на добавление hitDto {}", hitDto);

        Hit hit = repository.save(toHit(hitDto));
        log.info("Добавлен новый запрос {}", hit);
        return toHitDto(hit);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        if (Boolean.TRUE.equals(unique) && uris == null) {
            return repository.findUniqueIpStats(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern));
        } else if (Boolean.TRUE.equals(unique) && uris != null) {
            return repository.findUniqueIpAndUriStats(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern), uris);
        } else if (Boolean.FALSE.equals(unique) && uris == null) {
            return repository.findAll(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern));
        } else {
            return repository.findUriStats(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern), uris);
        }
    }
}
