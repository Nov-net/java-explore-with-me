package ru.practicum.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class HitDtoJsonTest {
    @Autowired
    JacksonTester<HitDto> json;

    @Test
    void testHit() throws Exception {
        HitDto hit = new HitDto("ewm-main-service", "/events/3", "100.100.0.1", "2022-01-01 02:00:00");
        JsonContent<HitDto> result = json.write(hit);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(hit.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(hit.getUri());
        assertThat(result).extractingJsonPathValue("$.ip").isEqualTo(hit.getIp());
        assertThat(result).extractingJsonPathValue("$.timestamp").isEqualTo(hit.getTimestamp());
    }

    @Test
    void testHitNoArgs() throws Exception {
        HitDto hit = new HitDto();
        JsonContent<HitDto> result = json.write(hit);

        assertThat(result).extractingJsonPathStringValue("$.app").isNull();
        assertThat(result).extractingJsonPathStringValue("$.uri").isNull();
        assertThat(result).extractingJsonPathValue("$.ip").isNull();
        assertThat(result).extractingJsonPathValue("$.timestamp").isNull();

        hit.setApp("ewm-main-service");
        hit.setUri("/events/3");
        hit.setIp("100.100.0.1");
        hit.setTimestamp("2022-01-01 02:00:00");

        JsonContent<HitDto> result2 = json.write(hit);

        assertThat(result2).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(result2).extractingJsonPathStringValue("$.uri").isEqualTo("/events/3");
        assertThat(result2).extractingJsonPathValue("$.ip").isEqualTo("100.100.0.1");
        assertThat(result2).extractingJsonPathValue("$.timestamp").isEqualTo(hit.getTimestamp());
    }

    @Test
    void testHitBilder() throws Exception {
        HitDto hit = HitDto.builder()
                .app("ewm-main-service")
                .uri("/events/3")
                .ip("100.100.0.1")
                .timestamp("2022-01-01 02:00:00")
                .build();

        JsonContent<HitDto> result = json.write(hit);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo("/events/3");
        assertThat(result).extractingJsonPathValue("$.ip").isEqualTo("100.100.0.1");
        assertThat(result).extractingJsonPathValue("$.timestamp").isEqualTo(hit.getTimestamp());
    }
}
