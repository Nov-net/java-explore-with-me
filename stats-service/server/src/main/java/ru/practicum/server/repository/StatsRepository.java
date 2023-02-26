package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.StatsDto;
import ru.practicum.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    Hit save(Hit hit);

    @Query("select new ru.practicum.dto.StatsDto(h.app, h.uri, count(h.id)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.id) desc")
    List<StatsDto> findAll(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatsDto(h.app, h.uri, count(h.id)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(h.id) desc")
    List<StatsDto> findUriStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dto.StatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatsDto> findUniqueIpStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatsDto> findUniqueIpAndUriStats(LocalDateTime start, LocalDateTime end, List<String> uris);

}
