package com.example.backend.dto.exif;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExifDataResponse {
    private String dateTimeOriginal;
    private Double latitude;
    private Double longitude;
}
