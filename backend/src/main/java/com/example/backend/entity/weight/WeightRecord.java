package com.example.backend.entity.weight;

import com.example.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weight_records")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double weight; // 몸무게

    @Column(nullable = false)
    private LocalDateTime measuredAt; // 측정 날짜 및 시간

    @Column(nullable = false)
    private Integer fastingHours; // 공복 유지 시간 (단위: 시간)

    public void update(Double weight, LocalDateTime measuredAt, Integer fastingHours) {
        this.weight = weight;
        this.measuredAt = measuredAt;
        this.fastingHours = fastingHours;
    }
}
