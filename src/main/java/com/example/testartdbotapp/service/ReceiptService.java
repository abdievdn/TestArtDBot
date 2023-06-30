package com.example.testartdbotapp.service;

import com.example.testartdbotapp.model.Receipt;
import com.example.testartdbotapp.repository.ReceiptsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ReceiptService {

    private final ReceiptsRepository receiptsRepository;

    public void saveReceipts(Long value) {
        Receipt receipt = Receipt.builder()
                .value(value)
                .date(LocalDateTime.now())
                .build();
        receiptsRepository.save(receipt);
    }

    public void showReceipts(Long chatId, Integer year, Integer month) {
        Receipt receipt = new Receipt();

    }
}
