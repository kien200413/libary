package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.OrderDetailDTO;
import com.example.backendthuvien.DTO.Order_DetaiDTO;
import com.example.backendthuvien.Repositories.OrderRepositoris;
import com.example.backendthuvien.Repositories.Order_detailRepository;
import com.example.backendthuvien.Repositories.ProductReposutiry;
import com.example.backendthuvien.entity.Order;
import com.example.backendthuvien.entity.Order_Detail;
import com.example.backendthuvien.entity.Product;
import com.example.backendthuvien.exceptions.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderDetailService implements iOrderDetail{
    @Autowired
    private Order_detailRepository order_detailRepository;
    @Autowired
    private OrderRepositoris orderRepositoris;
    @Autowired
    private ProductReposutiry productRepositiry;
    @Override
    public Order_Detail creatOrderDetail(Order_DetaiDTO newOrder_detaiDTO) throws Exception {
        Order order=orderRepositoris.findById(newOrder_detaiDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("Khoong tim thay"+newOrder_detaiDTO.getOrderId()));
        Product product=productRepositiry.findById(newOrder_detaiDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("Khong tim thay" + newOrder_detaiDTO.getProductId()));
        Order_Detail order_detail=Order_Detail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(newOrder_detaiDTO.getNumberOfProducts())
                .price(newOrder_detaiDTO.getPrice())
                .totalMoney(newOrder_detaiDTO.getTotalMoney())
                .color(newOrder_detaiDTO.getColor())
                .build();
        order_detailRepository.save(order_detail);
        return order_detail;
    }

    @Override
    public Order_Detail getOrderDetail(long id) {

        return order_detailRepository.findById(id).orElseThrow(()->
                new DateTimeException("khong tim thay" +id));
    }

    @Override
    @Transactional
    public Order_Detail updateOrderDetail(long id, Order_DetaiDTO newOrder_detaiDTO) throws DataNotFoundException {
        //Tìm order có tồn tại hay không
        Order_Detail existingOrderDetail=order_detailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Không tìm thấy"+id));
        Order existingOrder=orderRepositoris.findById(newOrder_detaiDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Không tìm thấy"+newOrder_detaiDTO.getOrderId()));
        Product existingProduct=productRepositiry.findById(newOrder_detaiDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("Khong tim thay" + newOrder_detaiDTO.getProductId()));
        existingOrderDetail.setPrice(newOrder_detaiDTO.getPrice());
        existingOrderDetail.setTotalMoney(newOrder_detaiDTO.getTotalMoney());
        existingOrderDetail.setColor(newOrder_detaiDTO.getColor());
        existingOrderDetail.setNumberOfProducts(newOrder_detaiDTO.getNumberOfProducts());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);


        return order_detailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(long id) {
order_detailRepository.deleteById(id);
    }


    @Override
    public List<Order_Detail> findByOrderId(long orderId) {
        return order_detailRepository.findByOrderId(orderId);
    }


}
