package com.example.backend.service.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KisApiService {

    @Value("${kis.api.appkey}")
    private String appKey;

    @Value("${kis.api.appsecret}")
    private String appSecret;

    @Value("${kis.api.baseurl}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private String accessToken;

    /**
     * KIS API Access Token 발급
     */
    public synchronized String getAccessToken() {
        if (accessToken != null) return accessToken;

        String url = baseUrl + "/oauth2/tokenP";
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("appkey", appKey);
        body.put("appsecret", appSecret);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                accessToken = (String) response.getBody().get("access_token");
                log.info("KIS Access Token 발급 성공");
                return accessToken;
            }
        } catch (Exception e) {
            log.error("KIS Access Token 발급 실패: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 국내주식 현재가 조회
     */
    public Double getCurrentPrice(String stockCode) {
        String token = getAccessToken();
        if (token == null) return 0.0;

        String url = baseUrl + "/uapi/domestic-stock/v1/quotations/inquire-price?fid_cond_mrkt_div_code=J&fid_input_iscd=" + stockCode;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("authorization", "Bearer " + token);
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", "FHKST01010100"); // 주식 현재가 시세 TR ID

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map output = (Map) response.getBody().get("output");
                if (output != null) {
                    // stck_prpr가 현재가 필드명입니다.
                    return Double.parseDouble((String) output.get("stck_prpr"));
                }
            }
        } catch (Exception e) {
            log.error("주식 시세 조회 실패 ({}): {}", stockCode, e.getMessage());
        }
        return 0.0;
    }
}
