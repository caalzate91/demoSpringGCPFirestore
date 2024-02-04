package com.camiloalzate.application.service;

import com.camiloalzate.application.dto.User;
import com.camiloalzate.domain.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Flux<User> getAllUsers() {
        LOGGER.info("Getting all users from Firestore since UserService");
        return userRepository.retrieveAll().onErrorReturn(new User());
    }

    public Mono<User> getUserById(String id) {
        return userRepository.get(id).onErrorMap(e -> new Exception("Error occurred while getting user"));
    }

    public Mono<Boolean> saveUser(User user) {
        return userRepository.save(user).onErrorMap(e -> new Exception("Error occurred while saving user"));
    }

    public Mono<Void> deleteUser(String id) {
        try {
            userRepository.delete(id);
            return Mono.empty();
        } catch (Exception e) {
            return Mono.error(new Exception("Error occurred while deleting user"));
        }
    }
}