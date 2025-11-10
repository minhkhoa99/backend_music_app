package com.musicmanagement.service;

import com.musicmanagement.dto.MusicGenreDTO;

import java.util.List;

/**
 * Service interface cho MusicGenre
 */
public interface MusicGenreService {

    /**
     * Tạo mới thể loại nhạc
     */
    MusicGenreDTO createGenre(MusicGenreDTO genreDTO);

    /**
     * Cập nhật thể loại nhạc
     */
    MusicGenreDTO updateGenre(Long id, MusicGenreDTO genreDTO);

    /**
     * Xóa thể loại nhạc
     */
    void deleteGenre(Long id);

    /**
     * Lấy thông tin thể loại theo ID
     */
    MusicGenreDTO getGenreById(Long id);

    /**
     * Lấy thông tin thể loại theo mã
     */
    MusicGenreDTO getGenreByCode(String genreCode);

    /**
     * Lấy danh sách tất cả thể loại
     */
    List<MusicGenreDTO> getAllGenres();

    /**
     * Kiểm tra tồn tại theo mã
     */
    boolean existsByGenreCode(String genreCode);
}

