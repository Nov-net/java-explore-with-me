package ru.practicum.ewm.user.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> mapToUserDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public static User toUser(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail() != null ? userDto.getEmail() : null)
                .build();
        return user;
    }

    public static User userBilder(User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}