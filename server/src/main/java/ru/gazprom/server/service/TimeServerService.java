package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TimeServerService {

    private final RestTemplate restTemplate;

    private final String URL = "http://localhost:8081/api/time";

    public String getTimeFromMicroService() {
        return restTemplate.getForObject(URL, String.class);
    }

}
