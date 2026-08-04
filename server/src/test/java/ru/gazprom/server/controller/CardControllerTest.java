package ru.gazprom.server.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gazprom.server.service.CardService;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    CardService cardService;

    @Test
    void getAll() throws Exception {
        mvc.perform(get("/api/cards"))
                .andExpect(status().isOk());
    }

    @Test
    void getCard() {
    }

    @Test
    void createCard() {
    }

    @Test
    void deleteCard() {
    }

    @Test
    void updateCard() {
    }
}