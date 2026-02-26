package com.example.backend.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStockResponse {
    private Long id;
    private String stockCode;
    private String stockName;
    private Double averagePrice;
    private Integer quantity;
    private Double currentPrice; // 프론트에서 계산에 활용
}
