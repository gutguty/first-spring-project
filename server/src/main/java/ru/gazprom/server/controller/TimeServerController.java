package ru.gazprom.server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprom.server.service.TimeServerService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimeServerController {
    private final TimeServerService timeServerService;

    @GetMapping("/time-server")
    public String getTimeFromMicroService() {
        return timeServerService.getTimeFromMicroService();
    }
}
