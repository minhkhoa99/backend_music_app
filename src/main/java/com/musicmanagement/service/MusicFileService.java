package com.musicmanagement.service;

import com.musicmanagement.dto.MusicFileDTO;
import com.musicmanagement.dto.ReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface cho MusicFile
 */
public interface MusicFileService {

    /**
     * Tạo mới file nhạc
     */
    MusicFileDTO createMusicFile(MusicFileDTO musicFileDTO);

    /**
     * Cập nhật file nhạc
     */
    MusicFileDTO updateMusicFile(Long id, MusicFileDTO musicFileDTO);

    /**
     * Xóa file nhạc
     */
    void deleteMusicFile(Long id);

    /**
     * Lấy thông tin file nhạc theo ID
     */
    MusicFileDTO getMusicFileById(Long id);

    /**
     * Lấy thông tin file nhạc theo mã
     */
    MusicFileDTO getMusicFileByCode(String fileCode);

    /**
     * Lấy danh sách tất cả file nhạc (có phân trang)
     */
    Page<MusicFileDTO> getAllMusicFiles(Pageable pageable);

    /**
     * Tìm kiếm file nhạc theo từ khóa
     */
    Page<MusicFileDTO> searchMusicFiles(String keyword, Pageable pageable);

    /**
     * Lọc file nhạc theo thể loại
     */
    Page<MusicFileDTO> getMusicFilesByGenre(Long genreId, Pageable pageable);

    /**
     * Lọc file nhạc theo loại file
     */
    Page<MusicFileDTO> getMusicFilesByFileType(String fileType, Pageable pageable);

    /**
     * Lọc file nhạc theo nghệ sĩ
     */
    Page<MusicFileDTO> getMusicFilesByArtist(String artist, Pageable pageable);

    /**
     * Lọc file nhạc theo năm phát hành
     */
    Page<MusicFileDTO> getMusicFilesByReleaseYear(Integer year, Pageable pageable);

    /**
     * Upload file nhạc
     */
    MusicFileDTO uploadMusicFile(MultipartFile file, MusicFileDTO musicFileDTO);

    /**
     * Upload thumbnail
     */
    String uploadThumbnail(Long musicFileId, MultipartFile thumbnail);

    /**
     * Lấy danh sách file nhạc cũ hơn số năm chỉ định (ví dụ >40 năm)
     */
    List<MusicFileDTO> getOldMusicFiles(int minAge);

    /**
     * Báo cáo file nhạc theo thể loại
     */
    List<ReportDTO> getReportByGenre();

    /**
     * Báo cáo file nhạc theo năm
     */
    List<ReportDTO> getReportByYear();

    /**
     * Báo cáo tổng quan storage
     */
    ReportDTO getStorageReport();
}

