package com.example.backend.service.exif;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.example.backend.dto.exif.ExifDataResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class ExifService {

    public ExifDataResponse parseExifData(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            String dateTimeOriginal = null;
            Double latitude = null;
            Double longitude = null;

            // Extract Date and Time
            ExifSubIFDDirectory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (exifSubIFDDirectory != null) {
                // Use a specific timezone, for example, UTC. Or use server's default.
                Date date = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (date != null) {
                    // Format the date to a more standard string format
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    dateTimeOriginal = sdf.format(date);
                }
            }

            // Extract GPS Location
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if (gpsDirectory != null && gpsDirectory.getGeoLocation() != null) {
                latitude = gpsDirectory.getGeoLocation().getLatitude();
                longitude = gpsDirectory.getGeoLocation().getLongitude();
            }

            return new ExifDataResponse(dateTimeOriginal, latitude, longitude);

        } catch (ImageProcessingException | IOException e) {
            // Log the exception and return a specific response or re-throw as a custom exception
            e.printStackTrace(); // Consider using a logger in a real application
            // For this example, we return a response with null values
            return new ExifDataResponse(null, null, null);
        }
    }
}
