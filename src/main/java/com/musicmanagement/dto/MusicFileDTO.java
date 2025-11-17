package com.musicmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO cho MusicFile
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicFileDTO {

    private Long id;

    @NotBlank(message = "Mã file không được để trống")
    @Size(max = 100, message = "Mã file không được vượt quá 100 ký tự")
    private String fileCode;

    @NotBlank(message = "Tên file không được để trống")
    @Size(max = 255, message = "Tên file không được vượt quá 255 ký tự")
    private String fileName;

    private String filePath;

    private String thumbnailPath;

    private String fileType;

    private Long genreId;

    private String genreName; // Tên thể loại (để hiển thị)

    private String downloadLink;

    private String description;

    private String artist;

    private String album;

    private Integer duration; // Thời lượng (giây)

    private Long fileSize; // Kích thước (bytes)

    private Integer releaseYear;

    private String ageRange; // Lứa tuổi người nghe phù hợp

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer age; // Độ tuổi của file nhạc
}

