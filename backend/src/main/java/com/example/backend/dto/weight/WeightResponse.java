package com.example.backend.dto.weight;

import com.example.backend.entity.weight.WeightRecord;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightResponse {
    private Long id;
    private Double weight;
    private LocalDateTime measuredAt;
    private Integer fastingHours;

    public static WeightResponse from(WeightRecord record) {
        return WeightResponse.builder()
                .id(record.getId())
                .weight(record.getWeight())
                .measuredAt(record.getMeasuredAt())
                .fastingHours(record.getFastingHours())
                .build();
    }
}
