package com.camiloalzate.ui.controller;

import com.camiloalzate.application.dto.User;
import com.camiloalzate.application.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/camilo")
    public String getCamilo() {
        return "Camilo";
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        LOGGER.info("Getting all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public Mono<Boolean> saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
}