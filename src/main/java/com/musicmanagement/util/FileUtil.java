package com.musicmanagement.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class cho xử lý file
 */
@Component
@Slf4j
public class FileUtil {

    @Value("${app.allowed.file.types}")
    private String allowedFileTypes;

    private final Tika tika = new Tika();

    /**
     * Lưu file nhạc
     */
    public String saveMusicFile(MultipartFile file, String uploadDir) throws IOException {
        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên file unique
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "." + fileExtension;

        // Lưu file
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("Saved music file: {}", filePath);
        return filePath.toString();
    }

    /**
     * Lưu hình ảnh (thumbnail)
     */
    public String saveImage(MultipartFile file, String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "." + fileExtension;

        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("Saved image file: {}", filePath);
        return filePath.toString();
    }

    /**
     * Xóa file
     */
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
            log.info("Deleted file: {}", filePath);
        }
    }

    /**
     * Validate file nhạc
     */
    public void validateMusicFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename is invalid");
        }

        String fileExtension = getFileExtension(filename).toLowerCase();
        List<String> allowedTypes = Arrays.asList(allowedFileTypes.split(","));

        if (!allowedTypes.contains(fileExtension)) {
            throw new IllegalArgumentException(
                "File type not allowed. Allowed types: " + String.join(", ", allowedTypes)
            );
        }

        // Validate size (max 100MB)
        long maxSize = 100 * 1024 * 1024; // 100MB in bytes
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 100MB");
        }
    }

    /**
     * Lấy extension của file
     */
    public String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * Trích xuất metadata từ file nhạc
     * (Đơn giản hóa - trong thực tế có thể dùng JAudioTagger)
     */
    public Map<String, Object> extractMetadata(String filePath) {
        Map<String, Object> metadata = new HashMap<>();

        try {
            File file = new File(filePath);

            // Detect MIME type
            String mimeType = tika.detect(file);
            metadata.put("mimeType", mimeType);

            // TODO: Sử dụng JAudioTagger để extract metadata chi tiết
            // AudioFile audioFile = AudioFileIO.read(file);
            // Tag tag = audioFile.getTag();
            // AudioHeader audioHeader = audioFile.getAudioHeader();
            // metadata.put("artist", tag.getFirst(FieldKey.ARTIST));
            // metadata.put("album", tag.getFirst(FieldKey.ALBUM));
            // metadata.put("duration", audioHeader.getTrackLength());

            log.debug("Extracted metadata from file: {}", filePath);

        } catch (Exception e) {
            log.warn("Failed to extract metadata from file: {}", filePath, e);
        }

        return metadata;
    }

    /**
     * Format file size từ bytes sang human-readable format
     */
    public String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    /**
     * Kiểm tra file có tồn tại không
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Lấy kích thước file
     */
    public long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }
}

