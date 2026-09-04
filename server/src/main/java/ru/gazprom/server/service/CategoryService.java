package ru.gazprom.server.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.CategoryDTO;
import ru.gazprom.server.exception.CategoryNotFoundError;
import ru.gazprom.server.exception.FieldRequiredError;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationError;
import ru.gazprom.server.mapper.CategoryMapper;
import ru.gazprom.server.model.Category;
import ru.gazprom.server.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public Response<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                .map(categoryMapper::categoryToDto)
                .collect(Collectors.toList());

        return responseSuccess("getAllCategories", categories);
    }

    public Response<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> responseSuccess("getCategoryById", categoryMapper.categoryToDto(category)))
                .orElseGet(() -> responseError("getCategoryById", (new CategoryNotFoundError("Category with " + id + " id not found"))));
    }


    @Transactional
    public Response<CategoryDTO> createCategory(Category category) {
        List<ValidationError> errors = new ArrayList<>();

        if (category.getName() == null || category.getName().isBlank()) {
            errors.add(new FieldRequiredError("name"));
        } else if (categoryRepository.existsByName(category.getName())) {
            errors.add(new FieldRequiredError("name  " + category.getName() + " already exists"));
        }
        if (!errors.isEmpty()) {
            return responseError("createCategory", errors);
        }

        Category saved = categoryRepository.save(category);

        return responseSuccess("createCategory", categoryMapper.categoryToDto(saved));
    }

    public Response<Void> deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {
            return responseError("deleteCategory", new CategoryNotFoundError("Category with " + id + " id not found"));
        }
        categoryRepository.deleteById(id);

        return responseSuccess("deleteCategory", null);
    }

    @Transactional
    public Response<CategoryDTO> updateCategoryById(Long id, Category newCategory) {

        return categoryRepository.findById(id)
                .map(existCategory -> findByID(existCategory, newCategory))
                .orElseGet(() -> responseError("updateCategoryById", new CategoryNotFoundError("Category with " + id + " id not found")));
    }

    private Response<CategoryDTO> findByID(Category existCategory, Category newCategory) {

            List<ValidationError> errors = new ArrayList<>();

            if (newCategory.getName() == null || newCategory.getName().isBlank()) {
                errors.add(new FieldRequiredError("name"));
            } else if (!existCategory.getName().equals(newCategory.getName())
                    && categoryRepository.existsByName(newCategory.getName())) {
                errors.add(new FieldRequiredError("name " + newCategory.getName() + " already exists"));
            }

            if (!errors.isEmpty()) {
                return responseError("updateCategoryById", errors);
            }

            existCategory.setName(newCategory.getName());
            Category updated = categoryRepository.save(existCategory);
            return responseSuccess("updateCategoryById", categoryMapper.categoryToDto(updated));
        }
}
