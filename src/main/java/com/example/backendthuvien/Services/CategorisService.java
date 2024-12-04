package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.categoryDTO;
import com.example.backendthuvien.Repositories.CategoriesRepositori;
import com.example.backendthuvien.entity.Categories;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategorisService implements iCategorisService{
    @Autowired
    private CategoriesRepositori categoriesRepositori;
    @Override
    public Categories createCategory(categoryDTO categories) {
        Categories newCategory=Categories.builder().name(categories.getName()).build();
        return categoriesRepositori.save(newCategory);
    }

    @Override
    public Categories getCategoryById(long id) {
        return categoriesRepositori.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepositori.findAll();
    }

    @Override
    public Categories updateCategories(@Validated long CategoriesId, @RequestBody categoryDTO categories) {
        Categories existingCate=getCategoryById(CategoriesId);
        existingCate.setName(categories.getName());
        categoriesRepositori.save(existingCate);
        return existingCate;

    }

    @Override
    public void deleteCategories(long id) {
         categoriesRepositori.deleteById(id);

    }
    public List<Categories> searchCategoriesByName(String name) {
        return categoriesRepositori.findByNameContainingIgnoreCase(name);
    }
}
