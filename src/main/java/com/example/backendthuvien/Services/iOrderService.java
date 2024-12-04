package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.OrderDTO;
import com.example.backendthuvien.DTO.ProductDTO;
import com.example.backendthuvien.entity.Order;
import com.example.backendthuvien.entity.Product;
import com.example.backendthuvien.exceptions.DataNotFoundException;
//import com.example.backendthuvien.reponse.OrderRespone;
import com.example.backendthuvien.reponse.ProductRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface iOrderService {
    Order creatOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(long id);

    Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(long id);

   List<Order> findByUsserId(long userId);
    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);
}
