package com.example.backend.repository.weight;

import com.example.backend.entity.weight.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
    List<WeightRecord> findAllByUserIdOrderByMeasuredAtDesc(Long userId);
}
