package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.CartItemsDTO;
import com.example.backendthuvien.DTO.OrderDTO;
import com.example.backendthuvien.DTO.Order_DetaiDTO;
import com.example.backendthuvien.Repositories.*;
import com.example.backendthuvien.entity.*;
import com.example.backendthuvien.exceptions.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements iOrderService {
    @Autowired
    private OrderRepositoris orderRepository;
    @Autowired
    private CouponRepo couponRepo;
@Autowired
private CouponService couponService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductReposutiry productReposutiry;
    @Autowired
    private Order_detailRepository order_detailRepository;
    private final ModelMapper modelMapper;

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);  // Truy vấn theo trạng thái
    }

    public List<Order> getOrdersByStatuses(List<String> statuses) {
        return orderRepository.findAllByStatusIn(statuses);  // Truy vấn theo nhiều trạng thái
    }
    @Override
    public Order creatOrder(OrderDTO orderDTO) throws Exception{
        // Tìm userId có tồn tại không
        User u = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không thấy id: " + orderDTO.getUserId()));

        // Mapping từ OrderDTO sang Order, bỏ qua trường id
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        Order o = new Order();
        modelMapper.map(orderDTO, o);

        // Set thông tin user và các thuộc tính cho order
        o.setUser(u);
        o.setOrderDate(new Date());  // ngày hiện tại là ngày đặt hàng
        o.setStatus(Order_status.PENDING); // trạng thái đơn hàng là PENDING

        // Kiểm tra và set ngày giao hàng
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Ngày giao hàng không hợp lệ. Ngày giao hàng không thể là ngày trong quá khứ.");
        }
        o.setShippingDate(shippingDate);

        // Đánh dấu đơn hàng là hoạt động
        o.setActive(true);

        // Kiểm tra và tính toán giảm giá (nếu có)
        float discountAmount = 0;
        if (orderDTO.getCouponCode() != null && !orderDTO.getCouponCode().isEmpty()) {
            Coupon coupon = couponService.validateCoupon(orderDTO.getCouponCode(), orderDTO.getTotalMoney());
            discountAmount = couponService.calculateDiscount(orderDTO, coupon);
            o.setCoupon(coupon);
            System.out.println("Discount Amount: " + discountAmount);
        } else {
            o.setCoupon(null);
        }
        float totalAfterDiscount = orderDTO.getTotalMoney() - discountAmount;
        o.setTotalMoney(totalAfterDiscount);
        System.out.println("Total After Discount: " + totalAfterDiscount);

        if (totalAfterDiscount < 0) {
            throw new IllegalArgumentException("Tổng tiền sau khi áp dụng giảm giá không thể âm.");
        }
        o.setTotalMoney(totalAfterDiscount);

        // Lưu đơn hàng vào repository
        orderRepository.save(o);
        List<Order_Detail> orderDetails = new ArrayList<>();
        for (CartItemsDTO cartItemsDTO : orderDTO.getCartItems()) {
            Order_Detail orderDetail = new Order_Detail();
            orderDetail.setOrder(o);
            Long productId = cartItemsDTO.getProductId();
            int quantity = cartItemsDTO.getQuantity();

            Product product = productReposutiry.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found with id:" + productId));
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }

        order_detailRepository.saveAll(orderDetails);

        return o;
    }

    private float calculateDiscount(OrderDTO orderDTO, Coupon coupon) {
        float discountAmount = 0;

        // Kiểm tra giá trị đơn hàng và loại giảm giá
        if (coupon != null && orderDTO.getTotalMoney() >= coupon.getMinOrderValue()) {
            if (coupon.getDiscountType() == DiscountType.percentage) {
                // Giảm giá theo phần trăm
                discountAmount = (coupon.getDiscountValue() / 100) * orderDTO.getTotalMoney();
            } else if (coupon.getDiscountType() == DiscountType.fixed) {
                // Giảm giá cố định
                discountAmount = coupon.getDiscountValue();
            }

            // Kiểm tra giới hạn giảm giá tối đa
            if (coupon.getMaxDiscountValue() != null && discountAmount > coupon.getMaxDiscountValue()) {
                discountAmount = coupon.getMaxDiscountValue();
            }
        }

        return discountAmount;
    }


    @Override
    public Order getOrder(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không thấy id: " + id));
        User exitingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không thấy id: " + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));


        modelMapper.map(orderDTO, order);
        order.setUser(exitingUser);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(long id) {
        Order order = orderRepository.findById(id).orElse(null);
//khoong xoa cung
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUsserId(long userId) {

        return orderRepository.findByUserId(userId);
    }

    @Override
    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) {
        return orderRepository.findByKeyword(keyword, pageable);
    }

}
