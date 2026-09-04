package ru.gazprom.server.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.CategoryDTO;
import ru.gazprom.server.exception.CategoryNotFoundException;
import ru.gazprom.server.exception.FieldRequiredException;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationException;
import ru.gazprom.server.mapper.CategoryMapper;
import ru.gazprom.server.model.Category;
import ru.gazprom.server.repository.CardRepository;
import ru.gazprom.server.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public Response<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                .map(categoryMapper::categoryToDto)
                .collect(Collectors.toList());

        return new Response<>(LocalDateTime.now(), "getAllCategories", true, categories, List.of());
    }

    public Response<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> new Response<>(LocalDateTime.now(), "getCategoryById", true, categoryMapper.categoryToDto(category), List.of()))
                .orElseGet(() -> new Response<>(LocalDateTime.now(), "getCategoryById", false, null,
                        List.of(new CategoryNotFoundException("Category with " + id + " id not found"))));
    }


    @Transactional
    public Response<CategoryDTO> createCategory(Category category) {
        List<ValidationException> errors = new ArrayList<>();

        if (category.getName() == null || category.getName().isBlank()) {
            errors.add(new FieldRequiredException("name"));
        } else if (categoryRepository.existsByName(category.getName())) {
            errors.add(new FieldRequiredException("name  " + category.getName() + " already exists"));
        }
        if (!errors.isEmpty()) {
            return new Response<>(LocalDateTime.now(), "createCategory", false, null, errors);
        }

        Category saved = categoryRepository.save(category);

        return new Response<>(LocalDateTime.now(), "createCategory", true, categoryMapper.categoryToDto(saved), List.of());
    }

    public Response<Void> deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {
            return new Response<>(LocalDateTime.now(), "deleteCategory", false, null,
                    List.of(new CategoryNotFoundException("Category with " + id + " id not found")));
        }
        categoryRepository.deleteById(id);

        return new Response<>(LocalDateTime.now(), "deleteCategory", true, null, List.of());
    }

    @Transactional
    public Response<CategoryDTO> updateCategoryById(Long id, Category newCategory) {

        return categoryRepository.findById(id)
                .map(existCategory -> {
                    List<ValidationException> errors = new ArrayList<>();

                    if (newCategory.getName() == null || newCategory.getName().isBlank()) {
                        errors.add(new FieldRequiredException("name"));
                    } else if (!existCategory.getName().equals(newCategory.getName())
                            && categoryRepository.existsByName(newCategory.getName())) {
                        errors.add(new FieldRequiredException("name " + newCategory.getName() + " already exists"));
                    }
                    if (!errors.isEmpty()) {
                        return new Response<>(LocalDateTime.now(), "updateCategoryById", false, (CategoryDTO) null, errors);
                    }

                    existCategory.setName(newCategory.getName());
                    Category updated = categoryRepository.save(existCategory);
                    return new Response<>(LocalDateTime.now(), "updateCategoryById", true, categoryMapper.categoryToDto(updated), List.of());
                })
                .orElseGet(() -> new Response<>(LocalDateTime.now(), "updateCategoryById", false, null,
                        List.of(new CategoryNotFoundException("Category with " + id + " id not found"))));
    }

}
