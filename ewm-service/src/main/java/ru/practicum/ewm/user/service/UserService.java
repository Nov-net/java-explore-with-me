package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    boolean deleteUser(Long userId);

    List<UserDto> getUser(List<Long> ids, Integer from, Integer size);

}
