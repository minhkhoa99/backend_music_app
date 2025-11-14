package com.musicmanagement.controller;

import com.musicmanagement.dto.MusicFileDTO;
import com.musicmanagement.dto.ReportDTO;
import com.musicmanagement.service.MusicFileService;
import com.musicmanagement.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * REST Controller cho MusicFile
 */
@RestController
@RequestMapping("/api/music-files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Music File", description = "APIs quản lý file nhạc")
public class MusicFileController {

    private final MusicFileService musicFileService;

    /**
     * Tạo mới file nhạc
     */
    @PostMapping
    @Operation(summary = "Tạo file nhạc mới")
    public ResponseEntity<MusicFileDTO> createMusicFile(@Valid @RequestBody MusicFileDTO musicFileDTO) {
        log.info("REST request to create music file: {}", musicFileDTO.getFileCode());
        MusicFileDTO result = musicFileService.createMusicFile(musicFileDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Upload file nhạc
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file nhạc")
    public ResponseEntity<MusicFileDTO> uploadMusicFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileCode") String fileCode,
            @RequestParam("fileName") String fileName,
            @RequestParam(value = "genreId", required = false) Long genreId,
            @RequestParam(value = "artist", required = false) String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "releaseYear", required = false) Integer releaseYear,
            @RequestParam(value = "description", required = false) String description) {

        log.info("REST request to upload music file: {}", fileName);

        MusicFileDTO musicFileDTO = new MusicFileDTO();
        musicFileDTO.setFileCode(fileCode);
        musicFileDTO.setFileName(fileName);
        musicFileDTO.setGenreId(genreId);
        musicFileDTO.setArtist(artist);
        musicFileDTO.setAlbum(album);
        musicFileDTO.setReleaseYear(releaseYear);
        musicFileDTO.setDescription(description);

        MusicFileDTO result = musicFileService.uploadMusicFile(file, musicFileDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Upload thumbnail cho file nhạc
     */
    @PostMapping(value = "/{id}/thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload thumbnail cho file nhạc")
    public ResponseEntity<String> uploadThumbnail(
            @PathVariable Long id,
            @RequestParam("thumbnail") MultipartFile thumbnail) {

        log.info("REST request to upload thumbnail for music file ID: {}", id);
        String thumbnailPath = musicFileService.uploadThumbnail(id, thumbnail);
        return ResponseEntity.ok(thumbnailPath);
    }

    /**
     * Cập nhật file nhạc
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật file nhạc")
    public ResponseEntity<MusicFileDTO> updateMusicFile(
            @PathVariable Long id,
            @Valid @RequestBody MusicFileDTO musicFileDTO) {
        log.info("REST request to update music file with ID: {}", id);
        MusicFileDTO result = musicFileService.updateMusicFile(id, musicFileDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Xóa file nhạc
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa file nhạc")
    public ResponseEntity<Void> deleteMusicFile(@PathVariable Long id) {
        log.info("REST request to delete music file with ID: {}", id);
        musicFileService.deleteMusicFile(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lấy thông tin file nhạc theo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin file nhạc theo ID")
    public ResponseEntity<MusicFileDTO> getMusicFileById(@PathVariable Long id) {
        log.info("REST request to get music file with ID: {}", id);
        MusicFileDTO result = musicFileService.getMusicFileById(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thông tin file nhạc theo mã
     */
    @GetMapping("/code/{fileCode}")
    @Operation(summary = "Lấy thông tin file nhạc theo mã")
    public ResponseEntity<MusicFileDTO> getMusicFileByCode(@PathVariable String fileCode) {
        log.info("REST request to get music file with code: {}", fileCode);
        MusicFileDTO result = musicFileService.getMusicFileByCode(fileCode);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách tất cả file nhạc (có phân trang)
     */
    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả file nhạc")
    public ResponseEntity<Page<MusicFileDTO>> getAllMusicFiles(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get all music files with pagination");
        Page<MusicFileDTO> result = musicFileService.getAllMusicFiles(pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Tìm kiếm file nhạc
     */
    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm file nhạc theo từ khóa")
    public ResponseEntity<Page<MusicFileDTO>> searchMusicFiles(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to search music files with keyword: {}", keyword);
        Page<MusicFileDTO> result = musicFileService.searchMusicFiles(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Lọc file nhạc theo thể loại
     */
    @GetMapping("/filter/genre/{genreId}")
    @Operation(summary = "Lọc file nhạc theo thể loại")
    public ResponseEntity<Page<MusicFileDTO>> getMusicFilesByGenre(
            @PathVariable Long genreId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get music files by genre ID: {}", genreId);
        Page<MusicFileDTO> result = musicFileService.getMusicFilesByGenre(genreId, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Lọc file nhạc theo loại file
     */
    @GetMapping("/filter/type/{fileType}")
    @Operation(summary = "Lọc file nhạc theo loại file")
    public ResponseEntity<Page<MusicFileDTO>> getMusicFilesByFileType(
            @PathVariable String fileType,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get music files by file type: {}", fileType);
        Page<MusicFileDTO> result = musicFileService.getMusicFilesByFileType(fileType, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Lọc file nhạc theo nghệ sĩ
     */
    @GetMapping("/filter/artist")
    @Operation(summary = "Lọc file nhạc theo nghệ sĩ")
    public ResponseEntity<Page<MusicFileDTO>> getMusicFilesByArtist(
            @RequestParam String artist,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get music files by artist: {}", artist);
        Page<MusicFileDTO> result = musicFileService.getMusicFilesByArtist(artist, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Lọc file nhạc theo năm phát hành
     */
    @GetMapping("/filter/year/{year}")
    @Operation(summary = "Lọc file nhạc theo năm phát hành")
    public ResponseEntity<Page<MusicFileDTO>> getMusicFilesByYear(
            @PathVariable Integer year,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get music files by release year: {}", year);
        Page<MusicFileDTO> result = musicFileService.getMusicFilesByReleaseYear(year, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Download file nhạc
     */
    @GetMapping("/{id}/download")
    @Operation(summary = "Download file nhạc")
    public ResponseEntity<Resource> downloadMusicFile(@PathVariable Long id) {
        log.info("REST request to download music file with ID: {}", id);
        
        try {
            MusicFileDTO musicFile = musicFileService.getMusicFileById(id);
            Path filePath = Paths.get(musicFile.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = "application/octet-stream";
                
                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + musicFile.getFileName() + "." + musicFile.getFileType() + "\"")
                        .body(resource);
            } else {
                throw new ResourceNotFoundException("File not found: " + musicFile.getFilePath());
            }
        } catch (Exception e) {
            log.error("Error downloading file", e);
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }
}

