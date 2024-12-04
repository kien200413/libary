package com.example.backendthuvien.Controller;

import com.example.backendthuvien.DTO.OrderDetailDTO;
import com.example.backendthuvien.DTO.Order_DetaiDTO;
import com.example.backendthuvien.Services.OrderDetailService;
import com.example.backendthuvien.entity.Order_Detail;
import com.example.backendthuvien.exceptions.DataNotFoundException;
import com.example.backendthuvien.reponse.OrderDetailRespone;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders_details")
@RequiredArgsConstructor
public class OrderDetaiCtrl {
    @Autowired
    private OrderDetailService orderDetailService;




    @PostMapping("")
    public ResponseEntity<?> addOrderDetail(@Validated @RequestBody Order_DetaiDTO order_detaiDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(err);

            }
            Order_Detail newOrderDetail = orderDetailService.creatOrderDetail(order_detaiDTO);
            return ResponseEntity.ok().body(OrderDetailRespone.fromOrderDetail(newOrderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Validated @PathVariable("id") long id) {
        Order_Detail order_detail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailRespone.fromOrderDetail(order_detail));
    }

    //Lấy ra danh sách order của 1 order nào đó
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Validated @PathVariable("orderId") long orderId) {
        List<Order_Detail> order_details = orderDetailService.findByOrderId(orderId);
        List<OrderDetailRespone> orderDetailRespones = order_details.stream().
                map(order_detail -> OrderDetailRespone.fromOrderDetail(order_detail))
                .toList();
        return ResponseEntity.ok(orderDetailRespones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @PathVariable("id") long id,
                                    @RequestBody Order_DetaiDTO orderDetail) {
        try {
           Order_Detail order_detail= orderDetailService.updateOrderDetail(id,orderDetail);
            return ResponseEntity.ok(order_detail);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Validated @PathVariable("id") long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok(id + " đã xoá thành công");
    }

}
