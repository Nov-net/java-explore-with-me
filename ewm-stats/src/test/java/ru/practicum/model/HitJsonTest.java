package ru.practicum.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class HitJsonTest {
    @Autowired
    JacksonTester<Hit> json;

    @Test
    void testHit() throws Exception {
        Hit hit = new Hit(1L, "ewm-main-service", "/events/3", "100.100.0.1", LocalDateTime.of(2022,01,01,02,00,01));
        JsonContent<Hit> result = json.write(hit);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(hit.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(hit.getUri());
        assertThat(result).extractingJsonPathValue("$.ip").isEqualTo(hit.getIp());
        assertThat(result).extractingJsonPathValue("$.timestamp").isEqualTo(hit.getTimestamp().toString());
    }

    @Test
    void testHitNoArgs() throws Exception {
        Hit hit = new Hit();
        JsonContent<Hit> result = json.write(hit);

        assertThat(result).extractingJsonPathNumberValue("$.id").isNull();
        assertThat(result).extractingJsonPathStringValue("$.app").isNull();
        assertThat(result).extractingJsonPathStringValue("$.uri").isNull();
        assertThat(result).extractingJsonPathValue("$.ip").isNull();
        assertThat(result).extractingJsonPathValue("$.timestamp").isNull();

        hit.setId(1L);
        hit.setApp("ewm-main-service");
        hit.setUri("/events/3");
        hit.setIp("100.100.0.1");
        hit.setTimestamp(LocalDateTime.of(2022,01,01,02,00,01));

        JsonContent<Hit> result2 = json.write(hit);

        assertThat(result2).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result2).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(result2).extractingJsonPathStringValue("$.uri").isEqualTo("/events/3");
        assertThat(result2).extractingJsonPathValue("$.ip").isEqualTo("100.100.0.1");
        assertThat(result2).extractingJsonPathValue("$.timestamp").isEqualTo(hit.getTimestamp().toString());
    }

    @Test
    void testHitBilder() throws Exception {
        Hit hit = Hit.builder()
                .id(1L)
                .app("ewm-main-service")
                .uri("/events/3")
                .ip("100.100.0.1")
                .timestamp(LocalDateTime.of(2022,01,01,02,00,01))
                .build();

        JsonContent<Hit> result = json.write(hit);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo("/events/3");
        assertThat(result).extractingJsonPathValue("$.ip").isEqualTo("100.100.0.1");
        assertThat(result).extractingJsonPathValue("$.timestamp").isEqualTo(hit.getTimestamp().toString());
    }
}
