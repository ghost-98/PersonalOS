package com.example.backend.entity.stock;

import com.example.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_stocks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "stockCode"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String stockCode;

    @Column(nullable = false)
    private String stockName;

    @Column(nullable = false)
    private Double averagePrice;

    @Column(nullable = false)
    private Integer quantity;

    public void updateInfo(Double averagePrice, Integer quantity) {
        this.averagePrice = averagePrice;
        this.quantity = quantity;
    }
}
