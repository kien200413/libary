package com.example.backendthuvien.Repositories;

import com.example.backendthuvien.DTO.OrderDetailDTO;
import com.example.backendthuvien.DTO.Order_DetaiDTO;
import com.example.backendthuvien.DTO.StatisticsDTO;
import com.example.backendthuvien.entity.Order_Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Order_detailRepository extends JpaRepository<Order_Detail, Long> {
    List<Order_Detail> findByOrderId(long orderId);

//    @Query("SELECT od.id AS order_detail_id, od.order.id, o.user.id, o.fullName AS order_fullname, o.email AS order_email," +
//            " o.phoneNumber AS order_phone_number, o.address AS order_address, o.note AS order_note," +
//            " o.orderDate, o.status, o.totalMoney AS order_total_money, o.shippingMethod, " +
//            "o.shippingAddress, o.shippingDate, o.trackingNumber, o.paymentMethod, o.active AS order_active, " +
//            "od.product.id, p.name AS product_name, p.price AS product_price, od.price AS order_detail_price, " +
//            "od.numberOfProducts, od.totalMoney AS order_detail_total_money, od.color," +
//            " p.thumbnail AS product_thumbnail FROM Order_Detail od JOIN Order o ON od.order.id = o.id JOIN Product  p ON od.product.id = p.id" +
//            " WHERE od.id = :orderDetailID")
//    List<OrderDetailDTO> getAllOrderDetail(@Param("orderDetailID") long orderDetailID);


//    @Query("SELECT new com.example.backendthuvien.DTO.OrderDetailDTO(" +
//            "od.id, o.id, o.user.id, o.fullName, o.email, o.phoneNumber, o.address, o.note, o.orderDate, " +
//            "o.status, o.totalMoney, o.shippingMethod, o.shippingAddress, o.shippingDate, " +
//            "o.trackingNumber, o.paymentMethod, o.active, p.id, p.name, p.price, od.price, " +
//            "od.numberOfProducts, od.totalMoney, od.color, p.thumbnail) " +
//            "FROM Order_Detail od " +
//            "JOIN od.order o " +
//            "JOIN od.product p " +
//            "WHERE od.id = :orderDetailId")
//    OrderDetailDTO getOrderDetailById(@Param("orderDetailId") long orderDetailId);

    @Query("SELECT new com.example.backendthuvien.DTO.OrderDetailDTO(" +
            "od.id, o.id, o.user.id, o.fullName, o.email, o.phoneNumber, o.address, o.note, o.orderDate, " +
            "o.status, o.totalMoney, o.shippingMethod, o.shippingAddress, o.shippingDate, " +
            "o.trackingNumber, o.paymentMethod, o.active, p.id, p.name, p.price, od.price, " +
            "od.numberOfProducts, od.totalMoney, od.color, p.thumbnail) " +
            "FROM Order_Detail od " +
            "JOIN od.order o " +
            "JOIN od.product p " +
            "WHERE o.user.id = :userId")
    List<OrderDetailDTO> getOrderDetailsByUserId(@Param("userId") long userId);









    @Query(value = "SELECT p.name AS productName, " +
            "WEEK(o.order_date) AS weekNumber, " +
            "YEAR(o.order_date) AS year, " +
            "SUM(od.number_of_products) AS totalQuantity " +
            "FROM order_detail od " +
            "JOIN orders o ON od.order_id = o.id " +
            "JOIN product p ON od.product_id = p.id " +
            "GROUP BY p.name, YEAR(o.order_date), WEEK(o.order_date) " +
            "ORDER BY year, weekNumber, totalQuantity DESC",
            nativeQuery = true)
    List<Object[]> findWeeklyStatistics();

    // Thống kê theo tháng
    @Query(value = "SELECT p.name AS productName, " +
            "MONTH(o.order_date) AS month, " +
            "YEAR(o.order_date) AS year, " +
            "SUM(od.number_of_products) AS totalQuantity " +
            "FROM order_detail od " +
            "JOIN orders o ON od.order_id = o.id " +
            "JOIN product p ON od.product_id = p.id " +
            "GROUP BY p.name, YEAR(o.order_date), MONTH(o.order_date) " +
            "ORDER BY year, month, totalQuantity DESC",
            nativeQuery = true)
    List<Object[]> findMonthlyStatistics();
}
