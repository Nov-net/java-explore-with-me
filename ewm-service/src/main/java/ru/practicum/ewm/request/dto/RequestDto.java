package ru.practicum.ewm.request.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {

    String created; // Дата и время создания заявки example: 2022-09-06T21:10:05.432

    Long event; // Идентификатор события example: 1

    Long id; // Идентификатор заявки example: 3

    Long requester; // Идентификатор пользователя, отправившего заявку example: 2

    String status; // Статус заявки example: PENDING

}
