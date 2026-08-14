package ru.gazprom.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gazprom.server.dto.CategoryDTO;
import ru.gazprom.server.model.Category;
import ru.gazprom.server.service.CategoryService;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getAllCategories() throws Exception {
        List<CategoryDTO> categories = Arrays.asList(
                new CategoryDTO(null, "Shoes"),
                new CategoryDTO(null, "Hats")
        );

        when(categoryService.getAllCategories()).thenReturn(categories);

        mvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById() throws Exception {
        Long id = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(id, "Boots");

        when(categoryService.getCategoryById(id)).thenReturn(categoryDTO);

        mvc.perform(get("/api/categories/${id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(categoryService, times(1)).getCategoryById(id);
    }

    @Test
    void createCategory() throws Exception {
        Long id = 1L;

        Category category = new Category(id, "Skirts");
        var json = objectMapper.writeValueAsString(category);
        CategoryDTO categoryDTO = new CategoryDTO(id, "Skirts");

        when(categoryService.createCategory(any(Category.class))).thenReturn(categoryDTO);

        mvc.perform(post("/api/categories")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(categoryService).createCategory(any(Category.class));
    }

    @Test
    void deleteCategory() throws Exception {
        Long id = 1L;
        doNothing().when(categoryService).deleteCategory(id);
        mvc.perform(delete("/api/categories/{id}", id)).andExpect(status().isOk());
        verify(categoryService).deleteCategory(id);
    }

    @Test
    void updateCategoryById() throws Exception {
        Long id = 1L;
        Category category = new Category(id, "Skirts");
        var json = objectMapper.writeValueAsString(category);
        CategoryDTO categoryDTO = new CategoryDTO(id, "Jackets");


        when(categoryService.updateCategoryById(eq(id), any(Category.class))).thenReturn(categoryDTO);

        mvc.perform(put("/api/categories/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(categoryService, times(1)).updateCategoryById(eq(1L), any(Category.class));
    }
}