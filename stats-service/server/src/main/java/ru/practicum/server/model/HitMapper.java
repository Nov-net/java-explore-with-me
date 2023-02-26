package ru.practicum.server.model;

import ru.practicum.dto.HitDto;
import ru.practicum.server.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private HitMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(), pattern))
                .build();
    }

    public static HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp().toString())
                .build();
    }

}
