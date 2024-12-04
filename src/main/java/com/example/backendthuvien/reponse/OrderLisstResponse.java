package com.example.backendthuvien.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLisstResponse {
    private List<orderRespone> orders;
    private int totalPages;

}
