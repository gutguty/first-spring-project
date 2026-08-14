package ru.gazprom.service_time.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprom.service_time.service.TimeService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/time")
    public String getCurrentTime() {
        return timeService.getCurrentTime();
    }
}
