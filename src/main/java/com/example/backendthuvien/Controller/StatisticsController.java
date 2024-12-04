package com.example.backendthuvien.Controller;

import com.example.backendthuvien.DTO.StatisticsDTO;
import com.example.backendthuvien.Services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    // API Thống kê theo tuần
    @GetMapping("/weekly")
    public ResponseEntity<List<StatisticsDTO>> getWeeklyStatistics() {
        try {
            List<StatisticsDTO> stats = statisticsService.getWeeklyStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // API Thống kê theo tháng
    @GetMapping("/monthly")
    public ResponseEntity<List<StatisticsDTO>> getMonthlyStatistics() {
        try {
            List<StatisticsDTO> stats = statisticsService.getMonthlyStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
