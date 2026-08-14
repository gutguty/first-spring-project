package ru.gazprom.service_time.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeService {
    public TimeService() {
    }

    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }

}
