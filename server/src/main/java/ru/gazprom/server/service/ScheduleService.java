package ru.gazprom.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.gazprom.server.repository.CardRepository;

@Service
@Slf4j
public class ScheduleService {

    private final CardRepository cardRepository;

    public ScheduleService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Scheduled(fixedRate = 30000)
    public void logCards() {
        log.info("Cards: {}",  cardRepository.count());
    }
}
