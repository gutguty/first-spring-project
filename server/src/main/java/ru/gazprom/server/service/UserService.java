package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.model.User;
import ru.gazprom.server.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
