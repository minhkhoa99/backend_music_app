package com.musicmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO cho các báo cáo thống kê
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    // Báo cáo file nhạc cũ (>40 năm)
    private List<MusicFileDTO> oldMusicFiles;
    private Integer totalOldFiles;
    private Integer minAge;

    // Báo cáo theo thể loại
    private String genreName;
    private Long fileCount;
    private Long totalSize;

    // Báo cáo theo năm
    private Integer year;
    private Long fileCountByYear;

    // Báo cáo tổng quan
    private Long totalFiles;
    private Long totalStorageSize;
    private String formattedStorageSize; // e.g., "1.5 GB"

    /**
     * Constructor cho báo cáo theo thể loại
     */
    public ReportDTO(String genreName, Long fileCount, Long totalSize) {
        this.genreName = genreName;
        this.fileCount = fileCount;
        this.totalSize = totalSize;
    }

    /**
     * Constructor cho báo cáo theo năm
     */
    public ReportDTO(Integer year, Long fileCountByYear) {
        this.year = year;
        this.fileCountByYear = fileCountByYear;
    }
}

