package com.example.backend.controller.exif;

import com.example.backend.dto.exif.ExifDataResponse;
import com.example.backend.service.exif.ExifService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "EXIF API", description = "이미지 EXIF 정보 파싱")
@RestController
@RequestMapping("/api/exif")
@RequiredArgsConstructor
public class ExifController {

    private final ExifService exifService;

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 EXIF 파싱", description = "이미지 파일을 업로드하여 촬영 시간 및 위치 정보를 추출합니다.")
    public ResponseEntity<ExifDataResponse> parseExif(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ExifDataResponse exifData = exifService.parseExifData(image);
        return new ResponseEntity<>(exifData, HttpStatus.OK);
    }
}
