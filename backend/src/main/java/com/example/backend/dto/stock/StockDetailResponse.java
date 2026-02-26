package com.example.backend.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDetailResponse {
    private String stockCode;
    private String stockName;
    private Double currentPrice;
}
