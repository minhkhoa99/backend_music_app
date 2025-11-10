package com.musicmanagement.repository;

import com.musicmanagement.entity.MusicFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository cho MusicFile entity
 */
@Repository
public interface MusicFileRepository extends JpaRepository<MusicFile, Long> {

    /**
     * Tìm file nhạc theo mã
     */
    Optional<MusicFile> findByFileCode(String fileCode);

    /**
     * Kiểm tra tồn tại theo mã file
     */
    boolean existsByFileCode(String fileCode);

    /**
     * Tìm kiếm file nhạc theo tên hoặc nghệ sĩ (hỗ trợ phân trang)
     */
    @Query("SELECT m FROM MusicFile m WHERE " +
           "LOWER(m.fileName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.artist) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<MusicFile> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Lọc theo thể loại
     */
    Page<MusicFile> findByGenreId(Long genreId, Pageable pageable);

    /**
     * Lọc theo loại file
     */
    Page<MusicFile> findByFileType(String fileType, Pageable pageable);

    /**
     * Lọc theo nghệ sĩ
     */
    Page<MusicFile> findByArtistContainingIgnoreCase(String artist, Pageable pageable);

    /**
     * Lọc theo năm phát hành
     */
    Page<MusicFile> findByReleaseYear(Integer releaseYear, Pageable pageable);

    /**
     * Tìm file nhạc có năm phát hành nhỏ hơn một năm nhất định (để tìm file cũ)
     */
    @Query("SELECT m FROM MusicFile m WHERE m.releaseYear <= :year ORDER BY m.releaseYear ASC")
    List<MusicFile> findOldMusicFiles(@Param("year") Integer year);

    /**
     * Tìm file nhạc theo khoảng năm phát hành
     */
    @Query("SELECT m FROM MusicFile m WHERE m.releaseYear BETWEEN :startYear AND :endYear")
    List<MusicFile> findByReleaseYearBetween(
        @Param("startYear") Integer startYear,
        @Param("endYear") Integer endYear
    );

    /**
     * Đếm số lượng file theo thể loại
     */
    @Query("SELECT m.genre.genreName, COUNT(m) FROM MusicFile m GROUP BY m.genre.genreName")
    List<Object[]> countByGenre();

    /**
     * Đếm số lượng file theo năm phát hành
     */
    @Query("SELECT m.releaseYear, COUNT(m) FROM MusicFile m WHERE m.releaseYear IS NOT NULL GROUP BY m.releaseYear ORDER BY m.releaseYear DESC")
    List<Object[]> countByReleaseYear();

    /**
     * Tính tổng dung lượng file
     */
    @Query("SELECT SUM(m.fileSize) FROM MusicFile m")
    Long getTotalFileSize();

    /**
     * Lấy tổng dung lượng theo thể loại
     */
    @Query("SELECT m.genre.genreName, SUM(m.fileSize) FROM MusicFile m GROUP BY m.genre.genreName")
    List<Object[]> getTotalFileSizeByGenre();
}

