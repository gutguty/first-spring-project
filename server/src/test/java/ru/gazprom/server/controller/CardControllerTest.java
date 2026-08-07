package ru.gazprom.server.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gazprom.server.dto.CardDTO;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.service.CardService;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    CardService cardService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAll() throws Exception {
        List<CardDTO> cards = Arrays.asList(
                new CardDTO(1L, "Shoe", 1000, "shoe.jpg"),
                new CardDTO(2L, "Sneakers", 2000, "sneakers.jpg")
        );

        when(cardService.getAll()).thenReturn(cards);

        mvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].id").value(cards.getLast().getId()))
                .andExpect(jsonPath("$[1].title").value(cards.getLast().getTitle()))
                .andExpect(jsonPath("$[1].price").value(cards.getLast().getPrice()))
                .andExpect(jsonPath("$[1].image").value(cards.getLast().getImage()));
    }

    @Test
    void getCard() throws Exception {
        List<CardDTO> cards = Arrays.asList(
                new CardDTO(1L, "Shoe", 1000, "shoe.jpg"),
                new CardDTO(2L, "Sneakers", 2000, "sneakers.jpg")
        );

        when(cardService.getCard(1L)).thenReturn(cards.getFirst());

        mvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cards.getFirst().getId()))
                .andExpect(jsonPath("$.title").value(cards.getFirst().getTitle()))
                .andExpect(jsonPath("$.price").value(cards.getFirst().getPrice()))
                .andExpect(jsonPath("$.image").value(cards.getFirst().getImage()));
    }

    @Test
    void createCard() throws Exception {
        Card request = new Card(null,"Boots", 500, "boots.png", null, null);
        String json = objectMapper.writeValueAsString(request);
        CardDTO response = new CardDTO(1L,"Boots", 500, "boots.png");

        when(cardService.createCard(any(Card.class))).thenReturn(response);

        mvc.perform(post("/api/cards")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.price").value(response.getPrice()))
                .andExpect(jsonPath("$.image").value(response.getImage()));
    }

    @Test
    void deleteCard() throws Exception {
        doNothing().when(cardService).deleteCard(1L);

        mvc.perform(delete("/api/cards/1"))
                .andExpect(status().isOk());

        verify(cardService).deleteCard(1L);
    }

    @Test
    void updateCard() throws Exception {
        Card request = new Card(null, "Boots", 500, "boots.png", null, null);

        String json = objectMapper.writeValueAsString(request);
        CardDTO response = new CardDTO(1L,"Boots2", 5000, "boots2.png");

        when(cardService.updateCard(eq(1L), any(Card.class))).thenReturn(response);

        mvc.perform(put("/api/cards/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.price").value(response.getPrice()))
                .andExpect(jsonPath("$.image").value(response.getImage()));
    }
}