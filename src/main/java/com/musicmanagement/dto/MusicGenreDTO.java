package com.musicmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO cho MusicGenre
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicGenreDTO {

    private Long id;

    @NotBlank(message = "Mã thể loại không được để trống")
    @Size(max = 50, message = "Mã thể loại không được vượt quá 50 ký tự")
    private String genreCode;

    @NotBlank(message = "Tên thể loại không được để trống")
    @Size(max = 100, message = "Tên thể loại không được vượt quá 100 ký tự")
    private String genreName;

    private String description;

    private String ageRange; // Lựa tuổi nghe phù hợp (VD: "Mọi lứa tuổi", "13+", "18+")

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer totalFiles; // Tổng số file nhạc thuộc thể loại này
}

