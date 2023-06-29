package com.example.testartdbotapp.service;

import com.example.testartdbotapp.model.User;
import com.example.testartdbotapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private Optional<User> getOptionalUser(Long chatId) {
        return userRepository.findById(chatId);
    }

    public void checkUser(Long chatId) {
        Optional<User> user = getOptionalUser(chatId);
        if (user.isEmpty()) {
            User newUser = User.builder()
                    .chatId(chatId)
                    .registrationTime(LocalDateTime.now())
                    .lastVisitTime(LocalDateTime.now())
                    .build();
            userRepository.save(newUser);
        } else {
            user.get().setLastVisitTime(LocalDateTime.now());
            userRepository.save(user.get());
        }
    }

    public String getUserInfo(Long chatId, Chat chat) {
        User user = getOptionalUser(chatId).orElseThrow();
        return "Имя пользователя: " + chat.getFirstName() + " " + chat.getLastName() + "\n" +
                "Дата регистрации в боте: " + user.getRegistrationTime() + "\n" +
                "Дата последнего посещения: " + user.getLastVisitTime();
    }
}
