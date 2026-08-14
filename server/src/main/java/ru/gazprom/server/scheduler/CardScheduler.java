package ru.gazprom.server.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.gazprom.server.repository.CardRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class CardScheduler {

    private final CardRepository cardRepository;

    @Scheduled(cron = "${app.schedule.cron}")
    public void logCards() {
        log.info("Cards: {}",  cardRepository.count());
    }
}

