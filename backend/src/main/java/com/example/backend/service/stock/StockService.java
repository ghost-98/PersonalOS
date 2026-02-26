package com.example.backend.service.stock;

import com.example.backend.dto.stock.*;
import com.example.backend.entity.stock.UserStock;
import com.example.backend.entity.user.User;
import com.example.backend.repository.stock.UserStockRepository;
import com.example.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {

    private final UserStockRepository userStockRepository;
    private final UserRepository userRepository;
    private final KisApiService kisApiService;

    /**
     * 키워드 기반 주식 종목 검색 (실시간 검색용)
     */
    public List<StockKeywordResponse> searchStocksByKeyword(String query) {
        // 실제 운영 시에는 DB의 종목 마스터 테이블이나 Redis 등에서 검색해야 합니다.
        // 현재는 예시 데이터를 사용합니다.
        List<StockKeywordResponse> mockStocks = List.of(
            new StockKeywordResponse("005930", "삼성전자"),
            new StockKeywordResponse("035420", "NAVER"),
            new StockKeywordResponse("035720", "카카오"),
            new StockKeywordResponse("000660", "SK하이닉스"),
            new StockKeywordResponse("005380", "현대차")
        );

        return mockStocks.stream()
                .filter(s -> s.getStockName().contains(query) || s.getStockCode().contains(query))
                .collect(Collectors.toList());
    }

    /**
     * 특정 종목의 상세 정보 조회
     */
    public StockDetailResponse getStockDetail(String stockCode) {
        // 종목명은 실제 운영 시 마스터 데이터에서 가져와야 하며, 여기서는 예시로 처리합니다.
        String stockName = getStockNameByCode(stockCode);
        Double currentPrice = kisApiService.getCurrentPrice(stockCode);

        return StockDetailResponse.builder()
                .stockCode(stockCode)
                .stockName(stockName)
                .currentPrice(currentPrice)
                .build();
    }

    private String getStockNameByCode(String stockCode) {
        // 임시 종목명 매핑 (실제로는 DB 조회 필요)
        return switch (stockCode) {
            case "005930" -> "삼성전자";
            case "035420" -> "NAVER";
            case "035720" -> "카카오";
            case "000660" -> "SK하이닉스";
            case "005380" -> "현대차";
            default -> "알 수 없는 종목";
        };
    }

    /**
     * 내 주식 리스트 조회 (현재가 정보 포함)
     */
    public List<UserStockResponse> getUserStocks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return userStockRepository.findAllByUser(user).stream()
                .map(stock -> UserStockResponse.builder()
                        .id(stock.getId())
                        .stockCode(stock.getStockCode())
                        .stockName(stock.getStockName())
                        .averagePrice(stock.getAveragePrice())
                        .quantity(stock.getQuantity())
                        .currentPrice(kisApiService.getCurrentPrice(stock.getStockCode())) // 실제 KIS API 호출
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStock(String username, String stockCode) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        userStockRepository.findByUserAndStockCode(user, stockCode)
                .ifPresent(userStockRepository::delete);
    }
}
