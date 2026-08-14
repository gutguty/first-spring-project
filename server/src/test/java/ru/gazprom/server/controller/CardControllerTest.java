package ru.gazprom.server.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gazprom.server.dto.CardDTO;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.service.CardService;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    CardService cardService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllCards() throws Exception {
        List<CardDTO> cards = Arrays.asList(
                new CardDTO(1L, "Shoe", 1000, "shoe.jpg"),
                new CardDTO(2L, "Sneakers", 2000, "sneakers.jpg")
        );

        when(cardService.getAllCards()).thenReturn(cards);

        mvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(cardService, times(1)).getAllCards();
    }

    @Test
    void getCardById() throws Exception {
        List<CardDTO> cards = Arrays.asList(
                new CardDTO(1L, "Shoe", 1000, "shoe.jpg"),
                new CardDTO(2L, "Sneakers", 2000, "sneakers.jpg")
        );

        when(cardService.getCardById(1L)).thenReturn(cards.getFirst());

        mvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(cardService, times(1)).getCardById(1L);
    }

    @Test
    void createCard() throws Exception {
        Card request = new Card(null,"Boots", 500, "boots.png", null);
        String json = objectMapper.writeValueAsString(request);
        CardDTO response = new CardDTO(1L,"Boots", 500, "boots.png");

        when(cardService.createCard(any(Card.class))).thenReturn(response);

        mvc.perform(post("/api/cards")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(cardService).createCard(any(Card.class));
    }

    @Test
    void deleteCard() throws Exception {
        doNothing().when(cardService).deleteCard(1L);

        mvc.perform(delete("/api/cards/1"))
                .andExpect(status().isOk());

        verify(cardService).deleteCard(1L);
    }

    @Test
    void updateCardById() throws Exception {
        Card request = new Card(null, "Boots", 500, "boots.png", null);

        String json = objectMapper.writeValueAsString(request);
        CardDTO response = new CardDTO(1L,"Boots2", 5000, "boots2.png");

        when(cardService.updateCardById(eq(1L), any(Card.class))).thenReturn(response);

        mvc.perform(put("/api/cards/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(cardService, times(1)).updateCardById(eq(1L), any(Card.class));
    }
}