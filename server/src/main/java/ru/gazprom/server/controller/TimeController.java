package ru.gazprom.server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprom.server.service.TimeService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimeController {
    private final TimeService timeService;

    @GetMapping("/time-server")
    public String getTimeFromMicroService() {
        return timeService.getTimeFromMicroService();
    }
}
