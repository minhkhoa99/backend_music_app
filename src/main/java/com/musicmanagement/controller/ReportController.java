package com.musicmanagement.controller;

import com.musicmanagement.dto.MusicFileDTO;
import com.musicmanagement.dto.ReportDTO;
import com.musicmanagement.service.MusicFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller cho các báo cáo thống kê
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reports", description = "APIs báo cáo và thống kê")
public class ReportController {

    private final MusicFileService musicFileService;

    /**
     * Báo cáo file nhạc cũ (>40 năm hoặc tùy chỉnh)
     */
    @GetMapping("/old-music")
    @Operation(summary = "Lấy danh sách file nhạc cũ (mặc định >40 năm)")
    public ResponseEntity<List<MusicFileDTO>> getOldMusicFiles(
            @RequestParam(defaultValue = "40") int minAge) {
        log.info("REST request to get music files older than {} years", minAge);
        List<MusicFileDTO> result = musicFileService.getOldMusicFiles(minAge);
        return ResponseEntity.ok(result);
    }

    /**
     * Báo cáo thống kê theo thể loại
     */
    @GetMapping("/by-genre")
    @Operation(summary = "Thống kê số lượng file nhạc theo thể loại")
    public ResponseEntity<List<ReportDTO>> getReportByGenre() {
        log.info("REST request to get report by genre");
        List<ReportDTO> result = musicFileService.getReportByGenre();
        return ResponseEntity.ok(result);
    }

    /**
     * Báo cáo thống kê theo năm phát hành
     */
    @GetMapping("/by-year")
    @Operation(summary = "Thống kê số lượng file nhạc theo năm phát hành")
    public ResponseEntity<List<ReportDTO>> getReportByYear() {
        log.info("REST request to get report by year");
        List<ReportDTO> result = musicFileService.getReportByYear();
        return ResponseEntity.ok(result);
    }

    /**
     * Báo cáo tổng quan storage
     */
    @GetMapping("/storage")
    @Operation(summary = "Thống kê dung lượng lưu trữ")
    public ResponseEntity<ReportDTO> getStorageReport() {
        log.info("REST request to get storage report");
        ReportDTO result = musicFileService.getStorageReport();
        return ResponseEntity.ok(result);
    }
}

