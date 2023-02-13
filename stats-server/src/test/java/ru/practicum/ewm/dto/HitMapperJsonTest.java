package ru.practicum.ewm.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.ewm.model.Hit;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class HitMapperJsonTest {
    @Autowired
    JacksonTester<HitDto> json;

    @Autowired
    JacksonTester<Hit> json1;

    HitDto hitDto = new HitDto("ewm-main-service", "/events/3", "100.100.0.1", "2022-01-01 02:00:01");
    Hit hit = new Hit(1L, "ewm-main-service", "/events/3", "100.100.0.1", LocalDateTime.of(2022,01,01,02,00,01));

    @Test
    void toHitTest() throws Exception {
        Hit hit1 = HitMapper.toHit(hitDto);
        JsonContent<Hit> result = json1.write(hit1);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(hit.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(hit.getUri());
        assertThat(result).extractingJsonPathValue("$.ip").isEqualTo(hit.getIp());
        assertThat(result).extractingJsonPathValue("$.timestamp").isEqualTo(hit.getTimestamp().toString());
    }

    @Test
    void toHitDtoTest() throws Exception {
        HitDto hit1 = HitMapper.toHitDto(hit);
        JsonContent<HitDto> result = json.write(hit1);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(hit.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(hit.getUri());
        assertThat(result).extractingJsonPathValue("$.ip").isEqualTo(hit.getIp());
        assertThat(result).extractingJsonPathValue("$.timestamp").isEqualTo(hit.getTimestamp().toString());
    }
}
