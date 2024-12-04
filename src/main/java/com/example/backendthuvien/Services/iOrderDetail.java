package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.OrderDTO;
import com.example.backendthuvien.DTO.OrderDetailDTO;
import com.example.backendthuvien.DTO.Order_DetaiDTO;
import com.example.backendthuvien.entity.Order;
import com.example.backendthuvien.entity.Order_Detail;
import com.example.backendthuvien.exceptions.DataNotFoundException;

import java.util.List;

public interface iOrderDetail {
    Order_Detail creatOrderDetail(Order_DetaiDTO newOrder_detaiDTO) throws Exception;
    Order_Detail getOrderDetail(long id);

    Order_Detail updateOrderDetail(long id, Order_DetaiDTO newOrder_detaiDTO) throws DataNotFoundException;

    void deleteOrderDetail(long id);

    List<Order_Detail> findByOrderId(long orderId);
}
