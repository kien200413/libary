package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.ProductDTO;
import com.example.backendthuvien.DTO.ProductImageDTO;
import com.example.backendthuvien.entity.Product;
import com.example.backendthuvien.entity.ProductImage;
import com.example.backendthuvien.exceptions.DataNotFoundException;
import com.example.backendthuvien.exceptions.Invalidparameterexception;
import com.example.backendthuvien.reponse.ProductRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface iProductService {
    public Product create(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(long id) ;
    Page<ProductRespone> getAllProduct(PageRequest pageRequest,String keyword,Long categoryId);

    Product update(long id,ProductDTO productDTO) throws Exception;

    void delete(long id);

    boolean existsByName(String name);
    public ProductImage creatProductImage(long productId, ProductImageDTO productImageDTO) throws Exception;

    List<Product> findProductByIds(List<Long> productIds);
}
