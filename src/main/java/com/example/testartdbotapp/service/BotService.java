package com.example.testartdbotapp.service;

import com.example.testartdbotapp.model.Receipt;
import com.example.testartdbotapp.model.User;
import com.example.testartdbotapp.repository.ReceiptsRepository;
import com.example.testartdbotapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BotService {

    private final UserRepository userRepository;
    private final ReceiptsRepository receiptsRepository;

    private Optional<User> getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public void checkUser(Long chatId) {
        Optional<User> user = getUserByChatId(chatId);
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

    public void saveReceipts(Long chatId, Long value) {
        User user = getUserByChatId(chatId).orElseThrow();
        Receipt receipt = Receipt.builder()
                .user(user)
                .value(value)
                .date(LocalDateTime.now())
                .build();
        receiptsRepository.save(receipt);
    }

    public String getUserInfo(Long chatId, Chat chat) {
        User user = getUserByChatId(chatId).orElseThrow();
        return "Имя пользователя: " + chat.getFirstName() + " " + chat.getLastName() + "\n" +
                "Дата регистрации в боте: " + user.getRegistrationTime() + "\n" +
                "Дата последнего посещения: " + user.getLastVisitTime();
    }

    public Long showReceiptValue(Long chatId, Integer year, Integer month) {
        long value = 0;
        for (Receipt r : getReceiptsList(chatId)) {
            log.info(r.getDate().getYear() + " " + r.getDate().getMonth().getValue());
            if (r.getDate().getYear() == year && r.getDate().getMonth().getValue() == month) {
                value = value + r.getValue();
            }
        }
        return value;
    }
    private List<Receipt> getReceiptsList(Long chatId) {
        User user = getUserByChatId(chatId).orElseThrow();
        return user.getReceipts();
    }
}
