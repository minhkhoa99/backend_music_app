package com.musicmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class cho Thể loại nhạc (Music Genre)
 */
@Entity
@Table(name = "music_genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genre_code", unique = true, nullable = false, length = 50)
    private String genreCode;

    @Column(name = "genre_name", nullable = false, length = 100)
    private String genreName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "age_range", length = 50)
    private String ageRange; // Lựa tuổi nghe phù hợp (VD: "Mọi lứa tuổi", "13+", "18+")

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationship với MusicFile (One to Many)
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MusicFile> musicFiles = new ArrayList<>();
}

