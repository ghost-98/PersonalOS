package com.example.backend.service.weight;

import com.example.backend.dto.weight.WeightRequest;
import com.example.backend.dto.weight.WeightResponse;
import com.example.backend.entity.user.User;
import com.example.backend.entity.weight.WeightRecord;
import com.example.backend.repository.user.UserRepository;
import com.example.backend.repository.weight.WeightRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeightService {

    private final WeightRecordRepository weightRecordRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("인증된 사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public Long createWeightRecord(WeightRequest request) {
        User user = getCurrentUser();

        WeightRecord record = WeightRecord.builder()
                .user(user)
                .weight(request.getWeight())
                .measuredAt(request.getMeasuredAt())
                .fastingHours(request.getFastingHours())
                .build();

        return weightRecordRepository.save(record).getId();
    }

    @Transactional(readOnly = true)
    public List<WeightResponse> getWeightHistory() {
        User user = getCurrentUser();

        return weightRecordRepository.findAllByUserIdOrderByMeasuredAtDesc(user.getId())
                .stream()
                .map(WeightResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateWeightRecord(Long recordId, WeightRequest request) {
        User user = getCurrentUser();
        WeightRecord record = weightRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("기록을 찾을 수 없습니다."));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        record.update(request.getWeight(), request.getMeasuredAt(), request.getFastingHours());
    }

    @Transactional
    public void deleteWeightRecord(Long recordId) {
        User user = getCurrentUser();
        WeightRecord record = weightRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("기록을 찾을 수 없습니다."));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        weightRecordRepository.delete(record);
    }
}
