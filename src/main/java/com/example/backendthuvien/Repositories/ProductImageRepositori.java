package com.example.backendthuvien.Repositories;

import com.example.backendthuvien.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepositori extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProductId(long productId);
}
