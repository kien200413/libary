package com.example.backendthuvien.Repositories;

import com.example.backendthuvien.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepositoris extends JpaRepository<Order,Long> {
    //tim cac don hang cua usser nao do
    List<Order> findByUserId(Long userId);
    @Query("SELECT o FROM Order o WHERE (:keyword IS NULL OR :keyword = '' OR " +
            "o.fullName LIKE %:keyword% " +
            "OR o.address LIKE %:keyword% " +
            "OR o.note LIKE %:keyword% " +
            "OR o.email LIKE %:keyword%)")
    Page<Order> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<Order> findByStatus(String status);  // Lọc theo trạng thái
    List<Order> findAllByStatusIn(List<String> statuses);  // Lọc theo nhiều trạng thái

}
