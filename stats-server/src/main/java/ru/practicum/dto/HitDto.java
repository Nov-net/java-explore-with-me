package ru.practicum.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {
    @NotNull
    @NotBlank
    @NotEmpty
    private String app;
    @NotNull
    @NotBlank
    @NotEmpty
    private String uri;
    @NotNull
    @NotBlank
    @NotEmpty
    private String ip;
    @NotNull
    @NotBlank
    @NotEmpty
    private String timestamp;
}
