package ru.practicum.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class StatsDtoJsonTest {
    @Autowired
    JacksonTester<StatsDto> json;

    @Test
    void testHit() throws Exception {
        StatsDto hit = new StatsDto("ewm-main-service", "/events/3", 3);
        JsonContent<StatsDto> result = json.write(hit);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(hit.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(hit.getUri());
        assertThat(result).extractingJsonPathValue("$.hits").isEqualTo(3);
    }

    @Test
    void testHitNoArgs() throws Exception {
        StatsDto hit = new StatsDto();
        JsonContent<StatsDto> result = json.write(hit);

        assertThat(result).extractingJsonPathStringValue("$.app").isNull();
        assertThat(result).extractingJsonPathStringValue("$.uri").isNull();
        assertThat(result).extractingJsonPathValue("$.hits").isEqualTo(0);

        hit.setApp("ewm-main-service");
        hit.setUri("/events/3");

        JsonContent<StatsDto> result2 = json.write(hit);

        assertThat(result2).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(result2).extractingJsonPathStringValue("$.uri").isEqualTo("/events/3");
    }

    @Test
    void testHitBilder() throws Exception {
        StatsDto hit = StatsDto.builder()
                .app("ewm-main-service")
                .uri("/events/3")
                .hits(3L)
                .build();

        JsonContent<StatsDto> result = json.write(hit);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo("/events/3");
        assertThat(result).extractingJsonPathValue("$.hits").isEqualTo(3);
    }
}
