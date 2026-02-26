package com.example.backend.controller.stock;

import com.example.backend.dto.stock.StockDetailResponse;
import com.example.backend.dto.stock.StockKeywordResponse;
import com.example.backend.dto.stock.UserStockRequest;
import com.example.backend.dto.stock.UserStockResponse;
import com.example.backend.service.stock.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Tag(name = "Stock API", description = "주식 검색 및 내 주식 관리 API")
public class StockController {

    private final StockService stockService;

    @GetMapping("/search")
    @Operation(summary = "주식 키워드 검색", description = "종목명 또는 종목 코드로 주식 리스트를 검색합니다. (실시간 자동완성용)")
    public ResponseEntity<List<StockKeywordResponse>> searchStocks(@RequestParam("query") String query) {
        return ResponseEntity.ok(stockService.searchStocksByKeyword(query));
    }

    @GetMapping("/detail/{stockCode}")
    @Operation(summary = "주식 상세 정보 조회", description = "종목 코드를 통해 해당 주식의 현재가 등 상세 정보를 조회합니다.")
    public ResponseEntity<StockDetailResponse> getStockDetail(@PathVariable("stockCode") String stockCode) {
        return ResponseEntity.ok(stockService.getStockDetail(stockCode));
    }

    @GetMapping("/me")
    @Operation(summary = "내 주식 조회", description = "현재 로그인된 사용자의 보유 주식 리스트와 실시간 시세를 조회합니다.")
    public ResponseEntity<List<UserStockResponse>> getMyStocks(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(stockService.getUserStocks(userDetails.getUsername()));
    }

    @PostMapping("/me")
    @Operation(summary = "내 주식 등록/수정", description = "보유 주식을 등록하거나 기존 정보를 업데이트합니다.")
    public ResponseEntity<String> registerStock(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserStockRequest request) {
        stockService.registerOrUpdateStock(userDetails.getUsername(), request);
        return ResponseEntity.ok("주식 정보가 저장되었습니다.");
    }

    @DeleteMapping("/me/{stockCode}")
    @Operation(summary = "내 주식 삭제", description = "보유 주식 목록에서 해당 종목을 삭제합니다.")
    public ResponseEntity<String> deleteStock(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String stockCode) {
        stockService.deleteStock(userDetails.getUsername(), stockCode);
        return ResponseEntity.ok("주식 정보가 삭제되었습니다.");
    }
}
