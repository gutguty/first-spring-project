package ru.gazprom.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.CategoryDTO;
import ru.gazprom.server.enums.SortType;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.Category;
import ru.gazprom.server.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public Response<List<CategoryDTO> > getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{id}")
    public Response<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categories")
    public Response<CategoryDTO> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @DeleteMapping("/categories/{id}")
    public Response<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @PutMapping("/categories/{id}")
    public Response<CategoryDTO> updateCategoryById(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategoryById(id, category);
    }

    @GetMapping("/categories/sorted")
    public Response<List<CategoryDTO>> getAllCategoriesSortedByDate(@RequestParam SortType sortType) {
        return categoryService.getAllCategoriesSortedByDate(sortType);
    }
}
