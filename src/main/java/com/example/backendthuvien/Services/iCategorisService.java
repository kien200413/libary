package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.categoryDTO;
import com.example.backendthuvien.entity.Categories;
import org.springframework.stereotype.Service;

import java.util.List;

public interface iCategorisService {
    Categories createCategory(categoryDTO categories);

    Categories getCategoryById(long id);

    List<Categories> getAllCategories();

    Categories updateCategories(long CategoriesId,categoryDTO categories);

    void deleteCategories(long id);
}
