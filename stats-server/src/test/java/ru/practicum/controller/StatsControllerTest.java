package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
public class StatsControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    StatsService service;

    @Autowired
    MockMvc mvc;

    HitDto hitDto = new HitDto("ewm-main-service", "/events/3", "100.100.0.1", "2022-01-01 02:00:00");
    StatsDto statsDto = new StatsDto("ewm-main-service", "/events/3", 3);

    @Test
    void saveHitTest() throws Exception {
        when(service.saveHit(any()))
                .thenReturn(hitDto);

        mvc.perform(MockMvcRequestBuilders.post("/hit")
                        .content(mapper.writeValueAsString(hitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.app", is(hitDto.getApp())))
                .andExpect(jsonPath("$.uri", is(hitDto.getUri())))
                .andExpect(jsonPath("$.ip", is(hitDto.getIp())))
                .andExpect(jsonPath("$.timestamp", is(hitDto.getTimestamp())));
    }

    @Test
    void saveNotValidHitDtoTest() throws Exception {
        when(service.saveHit(any()))
                .thenThrow(InvalidParameterException.class);

        mvc.perform(post("/hit")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStatsTest() throws Exception {
        when(service.getStats(any(), any(), any(), any()))
                .thenReturn(List.of(statsDto));

        mvc.perform(get("/stats")
                        .param("start", "2022-01-01 00:00:00")
                        .param("end", "2022-01-02 00:00:00")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].app", is(statsDto.getApp())))
                .andExpect(jsonPath("$[0].uri", is(statsDto.getUri())))
                .andExpect(jsonPath("$[0].hits", is(3)));
    }

    @Test
    void getStatsNotValidParam() throws Exception {
        when(service.getStats(any(), any(), any(), any()))
                .thenThrow(InvalidParameterException.class);

        mvc.perform(get("/stats")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
