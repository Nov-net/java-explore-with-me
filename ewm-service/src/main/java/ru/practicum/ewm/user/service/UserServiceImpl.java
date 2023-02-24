package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.ewm.validator.Validator.isValidUser;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    /*POST /admin/users - создание нового пользователя*/
    @Override
    public UserDto saveUser(UserDto userDto) {
        log.info("Получен новый userDto {}", userDto);
        User user = repository.save(UserMapper.toUser(userDto));
        log.info("Сохранен новый user {}", user);
        return UserMapper.toUserDto(user);
    }

    /*GET /admin/users - получение пользователей по списку id*/
    @Override
    public List<UserDto> getUser(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (ids != null) {
            return UserMapper.mapToUserDto(repository.findAllById(ids, pageable).toList());
        } else {
            return UserMapper.mapToUserDto(repository.findAll(pageable).toList());
        }
    }

    /*DELETE /admin/users/{userId} - удаление пользователя по userId*/
    @Override
    public boolean deleteUser(Long userId) {
        log.info("Запрос на удаление пользователя с id {}", userId);
        repository.delete(isValidUser(userId, repository.findById(userId)));
        log.info("Пользователь с id {} удален", userId);
        return true;
    }

}