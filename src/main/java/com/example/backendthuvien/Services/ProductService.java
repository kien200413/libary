package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.ProductDTO;
import com.example.backendthuvien.DTO.ProductImageDTO;
import com.example.backendthuvien.Repositories.CategoriesRepositori;
import com.example.backendthuvien.Repositories.ProductImageRepositori;
import com.example.backendthuvien.Repositories.ProductReposutiry;
import com.example.backendthuvien.entity.Categories;
import com.example.backendthuvien.entity.Product;
import com.example.backendthuvien.entity.ProductImage;
import com.example.backendthuvien.exceptions.DataNotFoundException;
import com.example.backendthuvien.exceptions.Invalidparameterexception;
import com.example.backendthuvien.reponse.ProductRespone;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ProductService implements iProductService {
    @Autowired
    private ProductReposutiry reposutiry;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductImageRepositori productImageRepositori;
    @Autowired
    private CategoriesRepositori categoriesRepositori;

    @Override
    public Product create(ProductDTO productDTO) throws DataNotFoundException {
        Categories ca = categoriesRepositori.findById(productDTO.getCategoryId()).orElseThrow(() ->
                new DataNotFoundException("Không tìm thấy Category ID"));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .author(productDTO.getAuthor())
                .thumbnail(productDTO.getThumbnail())
                .categories(ca)
                .catalogue(productDTO.getCatalogue())
                .description(productDTO.getDescription())
                .build();
        return reposutiry.save(newProduct);
    }

    @Override
    public Product getProductById(long id) {
        return reposutiry.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
    }


    @Override
    public Page<ProductRespone> getAllProduct(PageRequest pageRequest, String keyword, Long categoryId) {
        Page<Product> productsPage;
        productsPage = reposutiry.searchProducts(categoryId, keyword, pageRequest);
        return productsPage.map(ProductRespone::fromProduct
        );
    }

    @Override
    @Transactional
    public Product update(long id, ProductDTO productDTO) throws Exception {
        // Lấy sản phẩm hiện có từ cơ sở dữ liệu
        logger.info("Updating product with ID: {}", id);
        logger.info("Request by user with roles: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        Product exist = getProductById(id);

        if (exist == null) {
            throw new DataNotFoundException("Product with ID " + id + " not found");
        }

        // Tìm kiếm category dựa trên categoryId từ productDTO
        Categories ca = categoriesRepositori.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Category ID"));
        logger.info("Existing product data: {}", exist);
        // Cập nhật thông tin sản phẩm
        exist.setName(productDTO.getName());
        exist.setCategories(ca);
        exist.setAuthor(productDTO.getAuthor());
        exist.setPrice(productDTO.getPrice());
        exist.setCatalogue(productDTO.getCatalogue());
        exist.setDescription(productDTO.getDescription());
        exist.setThumbnail(productDTO.getThumbnail());
        logger.info("Updated product data: {}", exist);

        Product updatedProduct = reposutiry.save(exist);

        // Log kết quả lưu thành công
        logger.info("Product with ID {} successfully updated", updatedProduct.getId());
        return updatedProduct;
    }


    @Override
    public void delete(long id) {
        // Lấy sản phẩm trước khi xóa
        Product product = reposutiry.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        // Xóa sản phẩm
        reposutiry.delete(product);
    }

    @Override
    public boolean existsByName(String name) {

        return reposutiry.existsByName(name);
    }

    @Override
    public ProductImage creatProductImage(long productId, ProductImageDTO productImageDTO) throws Exception {
        // Kiểm tra nếu Product có tồn tại, nếu không ném ngoại lệ DataNotFoundException
        Product existingProduct = reposutiry.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Product với ID: " + productId));

        // Kiểm tra số lượng ảnh hiện tại của sản phẩm
        int size = productImageRepositori.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            // Đổi thành IllegalArgumentException hoặc tạo ngoại lệ tùy chỉnh
            throw new IllegalArgumentException("Sản phẩm không được phép có quá 7 ảnh");
        }

        // Kiểm tra xem URL của ảnh có hợp lệ không
        if (productImageDTO.getImageUrl() == null || productImageDTO.getImageUrl().isEmpty()) {
            throw new IllegalArgumentException("URL của ảnh không được để trống");
        }

        // Tạo ProductImage mới
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        System.out.println("Tạo ảnh sản phẩm cho Product ID: " + productId + " với URL: " + productImageDTO.getImageUrl());

        // Lưu ProductImage vào database và trả về đối tượng mới tạo
        return productImageRepositori.save(newProductImage);
    }

    @Override
    public List<Product> findProductByIds(List<Long> productIds) {
        return reposutiry.findProductsByIds(productIds);
    }

    public List<Product> searchByCatalogue(String catalogue) {
        return reposutiry.findByCatalogueContainingIgnoreCase(catalogue);
    }

}
