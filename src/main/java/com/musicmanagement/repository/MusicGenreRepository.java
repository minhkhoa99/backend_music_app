package com.musicmanagement.repository;

import com.musicmanagement.entity.MusicGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository cho MusicGenre entity
 */
@Repository
public interface MusicGenreRepository extends JpaRepository<MusicGenre, Long> {

    /**
     * Tìm thể loại nhạc theo mã
     */
    Optional<MusicGenre> findByGenreCode(String genreCode);

    /**
     * Kiểm tra tồn tại theo mã
     */
    boolean existsByGenreCode(String genreCode);

    /**
     * Kiểm tra tồn tại theo tên
     */
    boolean existsByGenreName(String genreName);
}

