package com.example.backend.controller.weight;

import com.example.backend.dto.weight.WeightRequest;
import com.example.backend.dto.weight.WeightResponse;
import com.example.backend.service.weight.WeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Weight", description = "몸무게 관리 API")
@RestController
@RequestMapping("/api/weights")
@RequiredArgsConstructor
public class WeightController {

    private final WeightService weightService;

    @Operation(summary = "몸무게 기록 추가", description = "새로운 몸무게 기록을 추가합니다. (공복 유지 시간 포함)")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody WeightRequest request) {
        return ResponseEntity.ok(weightService.createWeightRecord(request));
    }

    @Operation(summary = "몸무게 기록 조회", description = "현재 사용자의 몸무게 전체 기록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<WeightResponse>> getHistory() {
        return ResponseEntity.ok(weightService.getWeightHistory());
    }

    @Operation(summary = "몸무게 기록 수정", description = "기존의 몸무게 기록을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody WeightRequest request) {
        weightService.updateWeightRecord(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "몸무게 기록 삭제", description = "특정 몸무게 기록을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        weightService.deleteWeightRecord(id);
        return ResponseEntity.ok().build();
    }
}
