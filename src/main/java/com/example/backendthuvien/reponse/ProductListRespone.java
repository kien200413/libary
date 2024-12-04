package com.example.backendthuvien.reponse;

import com.example.backendthuvien.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListRespone {
    private List<ProductRespone> products;
    private int totalPages;

}
