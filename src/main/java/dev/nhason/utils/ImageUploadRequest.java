package dev.nhason.utils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageUploadRequest {

    private String hotelName;
    private List<ImageFile> files;

    public static class ImageFile {
        private String originalFilename;
        private String contentType;
        private String data;
    }
}
