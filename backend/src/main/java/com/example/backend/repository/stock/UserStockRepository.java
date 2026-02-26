package com.example.backend.repository.stock;

import com.example.backend.entity.stock.UserStock;
import com.example.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    List<UserStock> findAllByUser(User user);
    Optional<UserStock> findByUserAndStockCode(User user, String stockCode);
}
