package com.example.backendthuvien.Controller;

import com.example.backendthuvien.DTO.OrderDTO;
import com.example.backendthuvien.DTO.categoryDTO;
import com.example.backendthuvien.Services.CouponService;
import com.example.backendthuvien.Services.OrderService;
import com.example.backendthuvien.entity.Coupon;
import com.example.backendthuvien.entity.DiscountType;
import com.example.backendthuvien.entity.Order;

import com.example.backendthuvien.reponse.OrderLisstResponse;
import com.example.backendthuvien.reponse.orderRespone;
import com.example.backendthuvien.util.LocalizationUtils;
import com.example.backendthuvien.util.MessageKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderCtrl {
    @Autowired
    private OrderService orderService;
    private final LocalizationUtils localizationUtils;
@Autowired private CouponService couponService;
    @GetMapping("/orders/search")
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    @PostMapping("")

    public ResponseEntity<?> createOrder(@Validated @RequestBody OrderDTO orderDTO, BindingResult result) {
        // Kiểm tra lỗi trong dữ liệu đầu vào
        if (result.hasErrors()) {
            // Trả về danh sách các lỗi đầu vào
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result.getAllErrors());
        }

        try {
            // Gọi service để tạo đơn hàng
            Order newOrder = orderService.creatOrder(orderDTO);

            // Trả về đơn hàng đã tạo thành công
            return ResponseEntity.ok(newOrder);

        } catch (IllegalArgumentException ex) {
            // Xử lý các lỗi do logic không hợp lệ
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // Xử lý các lỗi hệ thống hoặc không mong đợi
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi: " + ex.getMessage());
        }
    }



    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOders(@Validated @PathVariable("user_id") long userID) {
        try {
            List<Order> orders = orderService.findByUsserId(userID);

            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOder(@Validated @PathVariable("id") long orderId) {
        try {
            Order exiting = orderService.getOrder(orderId);
            orderRespone a=orderRespone.fromOrder(exiting);
            return ResponseEntity.ok(a);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @PathVariable long id, @RequestBody OrderDTO orderDTO) {
        try {

            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Xóa thành công");
        return ResponseEntity.ok(response); // Trả về JSON thay vì plain text
    }

    @GetMapping("/get-orders-by-keyword")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderLisstResponse> getOrdersByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                // Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );

        Page<orderRespone> orderPage = orderService
                .getOrdersByKeyword(keyword, pageRequest)
                .map(orderRespone::fromOrder);

        // Lấy tổng số trang
        int totalPages = orderPage.getTotalPages();
        List<orderRespone> orderResponses = orderPage.getContent();

        return ResponseEntity.ok(OrderLisstResponse.builder()
                .orders(orderResponses)
                .totalPages(totalPages)
                .build());
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

}
