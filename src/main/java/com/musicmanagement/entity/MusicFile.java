package com.musicmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity class cho File nhạc (Music File)
 */
@Entity
@Table(name = "music_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_code", unique = true, nullable = false, length = 100)
    private String fileCode;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "thumbnail_path", length = 500)
    private String thumbnailPath;

    @Column(name = "file_type", length = 50)
    private String fileType;

    @Column(name = "download_link", length = 500)
    private String downloadLink;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "artist", length = 255)
    private String artist;

    @Column(name = "album", length = 255)
    private String album;

    @Column(name = "duration")
    private Integer duration; // Thời lượng (giây)

    @Column(name = "file_size")
    private Long fileSize; // Kích thước file (bytes)

    @Column(name = "release_year")
    private Integer releaseYear; // Năm phát hành

    @Column(name = "age_range", length = 50)
    private String ageRange; // Lứa tuổi người nghe phù hợp (VD: "18+", "40+", "Người cao tuổi")

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationship với MusicGenre (Many to One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private MusicGenre genre;

    /**
     * Tính độ tuổi của file nhạc (năm hiện tại - năm phát hành)
     */
    public Integer getAge() {
        if (releaseYear != null) {
            return LocalDateTime.now().getYear() - releaseYear;
        }
        return null;
    }

    /**
     * Kiểm tra xem file nhạc có tuổi trên một số năm nhất định không
     */
    public boolean isOlderThan(int years) {
        Integer age = getAge();
        return age != null && age > years;
    }
}

