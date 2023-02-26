package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.*;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Slf4j
public class AdminUserController {
    private final UserService service;

    /*POST /admin/users - создание нового пользователя*/
    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(service.saveUser(userDto), HttpStatus.CREATED);
    }

    /*GET /admin/users - получение пользователей по списку id*/
    @GetMapping()
    public ResponseEntity<Object> getUser(@RequestParam(required = false) List<Long> ids,
                                          @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                          @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ResponseEntity<>(service.getUser(ids, from, size), HttpStatus.OK);
    }

    /*DELETE /admin/users/{userId} - удаление пользователя по userId*/
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        return new ResponseEntity<>(service.deleteUser(userId), HttpStatus.NO_CONTENT);
    }

}
