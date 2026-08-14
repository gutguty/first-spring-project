package ru.gazprom.server.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.CategoryDTO;
import ru.gazprom.server.exception.CategoryNotFoundException;
import ru.gazprom.server.mapper.CategoryMapper;
import ru.gazprom.server.model.Category;
import ru.gazprom.server.repository.CardRepository;
import ru.gazprom.server.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .map(categoryMapper::categoryToDto)
                .orElseThrow(() -> new CategoryNotFoundException("Category with " + id + " id not found"));
    }

    @Transactional
    public CategoryDTO createCategory(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        }

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.categoryToDto(savedCategory);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category with " + id + " id not found");
        }

        categoryRepository.deleteById(id);
    }

    @Transactional
    public CategoryDTO updateCategoryById(Long id, Category newCategory) {
        Category existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with " + id + " id not found"));

        if (newCategory.getName() == null || newCategory.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (!existCategory.getName().equals(newCategory.getName()) && categoryRepository.existsByName(newCategory.getName())) {
            throw new IllegalArgumentException("Category with name " + newCategory.getName() + " already exists");
        }

        existCategory.setName(newCategory.getName());

        Category updatedCategory = categoryRepository.save(existCategory);

        return categoryMapper.categoryToDto(updatedCategory);
    }

}
