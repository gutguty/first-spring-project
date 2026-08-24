package ru.gazprom.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TimeService {

    private final RestTemplate restTemplate;

    private final String URL;

    public TimeService(@Value("${app.url.service-time}") String URL, RestTemplate restTemplate) {
        this.URL = URL;
        this.restTemplate = restTemplate;
    }


    public String getTimeFromMicroService() {
        return restTemplate.getForObject(URL + "/api/time", String.class);
    }

}
