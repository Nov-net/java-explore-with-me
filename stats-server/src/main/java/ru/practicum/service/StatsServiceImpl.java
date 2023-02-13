package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitMapper;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.Hit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public HitDto saveHit(HitDto hitDto) {
        log.info("Запрос на добавление hitDto {}", hitDto);

        Hit hit = repository.save(HitMapper.toHit(hitDto));
        log.info("Добавлен новый запрос {}", hit);
        return HitMapper.toHitDto(hit);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        if (unique && uris == null) {
            return repository.findUniqueIpStats(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern));
        } else if (unique && uris != null) {
            return repository.findUniqueIpAndUriStats(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern), uris);
        } else if (!unique && uris == null) {
            return repository.findAll(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern));
        } else {
            return repository.findUriStats(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern), uris);
        }
    }
}
