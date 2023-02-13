package ru.practicum.ewm.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.StatsDto;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.service.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatsServiceImplTest {
    @Mock
    private StatsRepository repository;

    @InjectMocks
    private StatsServiceImpl service;

    HitDto hitDto;
    Hit hit;
    StatsDto statsDto;
    String start;
    String end;

    @BeforeEach
    void before() {
        hitDto = new HitDto("ewm-main-service", "/events/3", "100.100.0.1", "2022-01-01 02:00:00");
        hit = new Hit(1L, "ewm-main-service", "/events/3", "100.100.0.1", LocalDateTime.of(2022,01,01,02,00,00));
        statsDto = new StatsDto("ewm-main-service", "/events/3", 3);
        start = "2022-01-01 00:00:00";
        end = "2022-01-02 00:00:00";
    }

    @Test
    void saveHitTest() {
        when(repository.save(any()))
                .thenReturn(hit);

        Assertions.assertEquals(service.saveHit(hitDto).getApp(), hitDto.getApp());
    }

    @Test
    void getStatsTest() {
        when(repository.findAll((LocalDateTime) any(), any()))
                .thenReturn(List.of(statsDto));

        Assertions.assertEquals(statsDto, service.getStats(start, end, null, false).get(0));

        when(repository.findUriStats(any(), any(), any()))
                .thenReturn(List.of(statsDto));

        Assertions.assertEquals(statsDto, service.getStats(start, end, List.of(hitDto.getUri()), false).get(0));

        when(repository.findUniqueIpStats(any(), any()))
                .thenReturn(List.of(statsDto));

        Assertions.assertEquals(statsDto, service.getStats(start, end, null, true).get(0));

        when(repository.findUniqueIpAndUriStats(any(), any(), any()))
                .thenReturn(List.of(statsDto));

        Assertions.assertEquals(statsDto, service.getStats(start, end, List.of(hitDto.getUri()), true).get(0));
    }

}
