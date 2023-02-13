package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    boolean deleteUser(Long userId);

    /*
    List<UserDto> getAllUsers();

    UserDto saveNewUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    UserDto getUserDtoById(long userId);


*/
}
