package com.example.backend.dto.weight;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightRequest {
    private Double weight;          // 몸무게
    private LocalDateTime measuredAt; // 측정 일시
    private Integer fastingHours;   // 공복 유지 시간
}
