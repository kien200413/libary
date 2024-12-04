package com.example.backendthuvien.Controller;

import com.example.backendthuvien.DTO.OrderDTO;
import com.example.backendthuvien.DTO.categoryDTO;
import com.example.backendthuvien.Services.CategorisService;
import com.example.backendthuvien.entity.Categories;
import com.example.backendthuvien.reponse.LoginRespone;
import com.example.backendthuvien.reponse.UpdateCategoryReponses;
import com.example.backendthuvien.util.LocalizationUtils;
import com.example.backendthuvien.util.MessageKey;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class categorisCtrl {

    private final LocalizationUtils localizationUtils;
    @Autowired
    private CategorisService categorisService;

    @PostMapping("")
    public ResponseEntity<?> add(@Validated @RequestBody categoryDTO c, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Trả về lỗi dưới dạng JSON
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Validation failed",
                    "errors", errors
            ));
        }

        // Tạo danh mục mới
        try {
            categorisService.createCategory(c);
            // Trả về phản hồi JSON khi thêm thành công
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "message", "Thêm thành công",
                    "data", c
            ));
        } catch (Exception e) {
            // Xử lý lỗi trong trường hợp có vấn đề khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Thêm thất bại",
                    "error", e.getMessage()
            ));
        }
    }


    @GetMapping("")
    public ResponseEntity<?> index() {
        try{
            List<Categories> cate = categorisService.getAllCategories();
            return ResponseEntity.ok(cate);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryReponses> update(@Validated @PathVariable long id, @RequestBody categoryDTO categoryDTO) {
        categorisService.updateCategories(id, categoryDTO);
        return ResponseEntity.ok().body(UpdateCategoryReponses.builder()
                .message(localizationUtils.getLocal(MessageKey.UPDATE_CATEGORY_SUCCESSFULLY))
                .build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id) {
        try {
            categorisService.deleteCategories(id);
            // Trả về JSON khi xóa thành công
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Xoá thành công",
                    "id", id
            ));
        } catch (Exception e) {
            // Xử lý lỗi khi xóa thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Xoá thất bại",
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        // Làm sạch giá trị name
        String sanitizedInput = name.trim().replaceAll("^,+|,+$", ""); // Loại bỏ dấu phẩy đầu/cuối và khoảng trắng
        if (sanitizedInput.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Name parameter cannot be empty or invalid"
            ));
        }

        try {
            List<Categories> categories = categorisService.searchCategoriesByName(sanitizedInput);
            if (categories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "No categories found with the name: " + sanitizedInput
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Categories found",
                    "data", categories
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "An error occurred while searching for categories",
                    "error", e.getMessage()
            ));
        }
    }



}
