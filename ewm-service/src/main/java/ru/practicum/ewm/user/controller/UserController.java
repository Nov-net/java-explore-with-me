package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Slf4j
public class UserController {
    private final UserService userService;

    /*POST /users - создание нового пользователя*/
    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.CREATED);
    }

    /*DELETE /users/{userId} - удаление пользователя по userId*/
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.NO_CONTENT);
    }

    /*
    *//*GET /users - получение списка пользователей*//*
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    /*PATCH /users/{userId} - обновление пользователя*//*
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    *//*GET /users/{userId} - получение пользователя по userId*//*
    @GetMapping("/{userId}")
    public UserDto getUserDtoById(@PathVariable(required = false) long userId) {
        return userService.getUserDtoById(userId);
    }
    */


}
