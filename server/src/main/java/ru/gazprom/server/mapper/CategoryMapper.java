package ru.gazprom.server.mapper;

import org.springframework.stereotype.Component;
import ru.gazprom.server.dto.CategoryDTO;
import ru.gazprom.server.model.Category;

@Component
public class CategoryMapper {
    public CategoryDTO categoryToDto(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
