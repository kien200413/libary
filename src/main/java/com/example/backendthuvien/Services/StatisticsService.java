package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.StatisticsDTO;
import com.example.backendthuvien.Repositories.Order_detailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class StatisticsService {
    @Autowired
    private Order_detailRepository order_detailRepository;
    public List<StatisticsDTO> getWeeklyStatistics() {
        List<Object[]> results = order_detailRepository.findWeeklyStatistics();
        return results.stream()
                .map(row -> new StatisticsDTO(
                        (String) row[0],          // productName
                        ((Number) row[1]).intValue(), // weekNumber
                        ((Number) row[2]).intValue(), // year
                        ((Number) row[3]).longValue() // totalQuantity
                ))
                .collect(Collectors.toList());
    }

    public List<StatisticsDTO> getMonthlyStatistics() {
        List<Object[]> results = order_detailRepository.findMonthlyStatistics();
        return results.stream()
                .map(row -> new StatisticsDTO(
                        (String) row[0],          // productName
                        ((Number) row[1]).intValue(), // month
                        ((Number) row[2]).intValue(), // year
                        ((Number) row[3]).longValue() // totalQuantity
                ))
                .collect(Collectors.toList());
    }
}
