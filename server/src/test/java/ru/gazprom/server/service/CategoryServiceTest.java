package ru.gazprom.server.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import ru.gazprom.server.dto.CategoryDTO;
import ru.gazprom.server.exception.CategoryNotFoundException;
import ru.gazprom.server.mapper.CategoryMapper;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.model.Category;
import ru.gazprom.server.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    CategoryMapper categoryMapper;

    //getAll
    @Test
    void getAllCategories() {
        List<Category> categoryList = Arrays.asList(
            new Category(null, "he"),
            new Category(null, "he2")
        );

        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(categoryMapper.categoryToDto(categoryList.getFirst())).thenReturn(new CategoryDTO(null, "he"));
        when(categoryMapper.categoryToDto(categoryList.getLast())).thenReturn(new CategoryDTO(null, "he2"));
        var result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        var arr = result.stream().map(CategoryDTO::getName).toList();
        assertTrue(arr.contains("he"));
        assertTrue(arr.contains("he2"));
    }

    //getById
    @Test
    void getCategoryById() {
        Category category = new Category(1L, "he3");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.categoryToDto(category)).thenReturn(new CategoryDTO(1L, "he3"));

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCategoryByInvalidId() {
        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(999L));
    }

    //create
    @Test
    void createCategorySuccess() {
        Category category = new Category(null, "Shirts");
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Shirts");

        when(categoryRepository.existsByName("Shirts")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.categoryToDto(category)).thenReturn(categoryDTO);

        var created = categoryService.createCategory(category);

        assertEquals("Shirts", created.getName());
        assertNotNull(created.getId());
    }

    @Test
    void createCategoryWithEmptyAndNull() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory(new Category(null, "")));

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory(new Category(null, " ")));

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory(new Category(null, null)));
    }

    @Test
    void createCategoryDuplicateName() {
        when(categoryRepository.existsByName("Shirts")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory(new Category(null, "Shirts")));
    }

    //delete

    @Test
    void deleteCategoryByIdSuccess() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        categoryService.deleteCategory(id);
        verify(categoryRepository).deleteById(id);

    }

    @Test
    void deleteCategoryByIdNotSuccess() {
        Long id = 999L;
        when(categoryRepository.existsById(id)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(id));

        verify(categoryRepository, never()).deleteById(any());
    }

    //update
    @Test
    void updateCategorySuccess() {
        Category category = new Category(1L, "Boots");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.categoryToDto(category)).thenReturn(new CategoryDTO(1L, "Sneakers"));

        var updated = categoryService.updateCategoryById(1L, new Category(null, "Sneakers"));

        assertEquals("Sneakers", updated.getName());
    }

    @Test
    void updateCategorySameName() {
        Category category = new Category(1L, "Boots");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.categoryToDto(category)).thenReturn(new CategoryDTO(1L, "Boots"));

        var updated = categoryService.updateCategoryById(1L, new Category(null, "Boots"));

        assertEquals("Boots", updated.getName());
    }

    @Test
    void updateCategoryDuplicateName() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "Boots")));
        when(categoryRepository.existsByName("Shoes")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.updateCategoryById(1L, new Category(null, "Shoes")));
    }

    @Test
    void updateCategoryEmptyName() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "Boots")));

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.updateCategoryById(1L, new Category(null, "")));
    }

    @Test
    void updateInvalidId() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategoryById(999L, new Category(null, "Sneakers")));
    }
}
