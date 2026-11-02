package ru.gazprom.server.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprom.server.dto.OllamaRequest;
import ru.gazprom.server.dto.OllamaRequestDescription;

@RestController
@RequestMapping("/ai")
public class OllamaController {

    private final ChatClient chatClient;

    public OllamaController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .defaultSystem("You are manager of shop")
                .build();
    }

    @PostMapping("/ask")
    public String ask(@RequestBody OllamaRequest request) {
        return chatClient.prompt()
                .user(request.getPrompt())
                .call()
                .content();
    }

    @PostMapping("/description")
    public String generateDescription(@RequestBody OllamaRequestDescription request) {
        String prompt = "Make description for good " + "Name " + request.getName()
                + "Category " + request.getCategory();

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
