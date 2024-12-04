package com.example.backendthuvien.Controller;

import ch.qos.logback.core.util.StringUtil;
import com.example.backendthuvien.DTO.ProductDTO;
import com.example.backendthuvien.DTO.ProductImageDTO;
import com.example.backendthuvien.Services.ExcelExportService;
import com.example.backendthuvien.Services.ProductService;
import com.example.backendthuvien.entity.Product;
import com.example.backendthuvien.entity.ProductImage;
import com.example.backendthuvien.exceptions.DataNotFoundException;
import com.example.backendthuvien.reponse.ProductListRespone;
import com.example.backendthuvien.reponse.ProductRespone;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductCtrl {
    @Autowired
    private ProductService productService;
    @Autowired
    private ExcelExportService excelExportService;
    @GetMapping("")
    public ResponseEntity<ProductListRespone> index(@RequestParam(value = "page") int page,
                                                    @RequestParam(value = "limit") int limit,
                                                    @RequestParam(defaultValue = "") String keyword,
                                                    @RequestParam(defaultValue = "0",name = "category_id") Long categoryId
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit,
//                Sort.by("createdAt").descending()
                Sort.by("id").ascending()


        );
        Page<ProductRespone> products = productService.getAllProduct(pageRequest, keyword, categoryId);
        int totalPage = products.getTotalPages();
        List<ProductRespone> listPro = products.getContent();
        return ResponseEntity.ok(ProductListRespone.builder()
                .products(listPro)
                .totalPages(totalPage)
                .build()
        );
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") long productId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            // Kiểm tra nếu productId không tồn tại
            Product existingProduct = productService.getProductById(productId);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }

            // Đảm bảo rằng files không null
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("K up quá 6 ảnh");
            }
            List<ProductImage> productImages = new ArrayList<>();

            System.out.println("Danh sách tệp: " + files);

            // Duyệt qua từng file
            for (MultipartFile file : files) {
                System.out.println("Tệp: " + file.getOriginalFilename() + ", Kích thước: " + file.getSize());

                // Kiểm tra file có kích thước bằng 0
                if (file.getSize() == 0) {
                    continue;
                }

                // Kiểm tra nếu file lớn hơn 10MB
                if (file.getSize() > 10 * 1024 * 1024) { // kích thước 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size 10MB");
                }

                // Kiểm tra định dạng file
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }

                // Lưu file và trả về tên file
                String fileName = storeFile(file); // Phương thức lưu file (cần được định nghĩa)

                // Tạo ProductImage và lưu vào CSDL
                ProductImage productIMG = productService.creatProductImage(existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build());

                productImages.add(productIMG);
            }

            // Trả về danh sách ProductImage đã được tạo
            return ResponseEntity.ok(productImages);

        } catch (Exception e) {
            System.err.println("Lỗi khi lưu ảnh sản phẩm: " + e.getMessage());
            // Bắt mọi lỗi và trả về thông báo lỗi chi tiết
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Validated @RequestBody ProductDTO productDTO,
                                 BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(err);
            }

            // Thêm sản phẩm
            Product newProduct = productService.create(productDTO);

            // Ghi log vào Excel
            String detail = String.format("Thêm sản phẩm: %s (ID: %d, Giá: %.2f)",
                    newProduct.getName(),
                    newProduct.getId(),
                    newProduct.getPrice());
            excelExportService.logAction("Thêm", detail, "Thành công");

            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            // Ghi log lỗi vào Excel
            excelExportService.logAction("Lỗi thêm", "Không thể thêm sản phẩm: " + productDTO.getName(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    private String storeFile(MultipartFile file) throws Exception {
if(!isImagesFile(file) || file.getOriginalFilename()==null){
    throw new IOException("Invalid image format");
}
        // Làm sạch tên tệp để loại bỏ các ký tự không hợp lệ
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Tạo tên tệp duy nhất bằng UUID để tránh trùng lặp tên tệp
        String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn tới thư mục lưu trữ tệp
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra xem thư mục có tồn tại hay không, nếu không tồn tại thì tạo mới
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir); }
        // Tạo đường dẫn đầy đủ cho tệp sẽ được lưu trữ
        java.nio.file.Path destination = uploadDir.resolve(uniqueFileName);
        // Sao chép tệp vào thư mục đích với tùy chọn ghi đè nếu tệp đã tồn tại
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        // Trả về tên tệp duy nhất sau khi lưu
        return uniqueFileName;
    }
    private Boolean isImagesFile(MultipartFile file) {
        String contenType = file.getContentType();
        return contenType != null && contenType.startsWith("image/");
    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            // Tạo đường dẫn tới ảnh trong thư mục "uploads"
            java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);

            // Tạo một resource từ đường dẫn ảnh
            UrlResource resource = new UrlResource(imagePath.toUri());

            // Kiểm tra nếu tệp tồn tại
            if (resource.exists()) {
                // Nếu tệp tồn tại, trả về ResponseEntity với nội dung là ảnh và định dạng là JPEG
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                // Nếu tệp không tồn tại, trả về mã 404
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Bắt các ngoại lệ và trả về mã 404 nếu có lỗi
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getproductById(@PathVariable("id") long productId) {
        try {
            Product product = productService.getProductById(productId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + productId);
            }
            ProductRespone response = ProductRespone.fromProduct(product);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log chi tiết lỗi
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id) {
        try {
            // Lấy chi tiết sản phẩm trước khi xóa
            Product product = productService.getProductById(id);

            // Xóa sản phẩm
            productService.delete(id);

            // Ghi log vào báo cáo
            String detail = String.format("Xóa sản phẩm: %s (ID: %d)", product.getName(), id);
            excelExportService.logAction("Xóa", detail, "Thành công");

            return ResponseEntity.ok(String.format("Sản phẩm '%s' (ID: %d) đã bị xóa.", product.getName(), id));
        } catch (RuntimeException e) {
            // Ghi log lỗi
            excelExportService.logAction("Lỗi xóa", "Không thể xóa sản phẩm (ID: " + id + ")", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/generateFakeProducts")
    public ResponseEntity<?> generateFakeProduct() {
        Faker faker = new Faker();
        for (int i = 0; i <= 1000000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(2, 5))
                    .build();
            try {
                productService.create(productDTO);
            } catch (DataNotFoundException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake ok");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @Validated @PathVariable("id") long id,
            @RequestBody ProductDTO productDTO
    ) {
        try {
            Product updatedProduct = productService.update(id, productDTO);
            return ResponseEntity.ok(updatedProduct); // Trả về đối tượng Product đã cập nhật
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        try {
            // Tách chuỗi ids thành một mảng các số nguyên
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong) // Chuyển đổi chuỗi thành Long
                    .collect(Collectors.toList()); // Thu thập thành danh sách

            // Gọi phương thức dịch vụ để tìm sản phẩm theo danh sách id
            List<Product> products = productService.findProductByIds(productIds);

            // Trả về danh sách sản phẩm
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi xấu
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportLogs() {
        try {
            // Đường dẫn cố định để lưu file
            String directoryPath = "E:\\thuctap\\duanbackend\\logs";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }

            // Tạo tên file với timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "product_log_" + timestamp + ".xlsx";
            String filePath = directoryPath + "\\" + fileName;

            // Gọi service để xuất file Excel
            excelExportService.exportToExcel(filePath);

            // Kiểm tra file có tồn tại hay không
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IOException("File không tồn tại!");
            }

            // Đọc file và trả về cho người dùng
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);

        } catch (IOException e) {
            e.printStackTrace(); // Log lỗi để debug
            return ResponseEntity.status(500).body("Không thể xuất file Excel!");
        }
    }


    @GetMapping("/search")
    public List<Product> searchProductsByCatalogue(@RequestParam String catalogue) {
        return productService.searchByCatalogue(catalogue);
    }



}
