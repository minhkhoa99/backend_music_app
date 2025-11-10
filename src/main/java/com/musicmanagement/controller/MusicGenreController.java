package com.musicmanagement.controller;

import com.musicmanagement.dto.MusicGenreDTO;
import com.musicmanagement.service.MusicGenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller cho MusicGenre
 */
@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Music Genre", description = "APIs quản lý thể loại nhạc")
public class MusicGenreController {

    private final MusicGenreService genreService;

    /**
     * Tạo mới thể loại nhạc
     */
    @PostMapping
    @Operation(summary = "Tạo thể loại nhạc mới")
    public ResponseEntity<MusicGenreDTO> createGenre(@Valid @RequestBody MusicGenreDTO genreDTO) {
        log.info("REST request to create music genre: {}", genreDTO.getGenreCode());
        MusicGenreDTO result = genreService.createGenre(genreDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Cập nhật thể loại nhạc
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thể loại nhạc")
    public ResponseEntity<MusicGenreDTO> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody MusicGenreDTO genreDTO) {
        log.info("REST request to update music genre with ID: {}", id);
        MusicGenreDTO result = genreService.updateGenre(id, genreDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Xóa thể loại nhạc
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa thể loại nhạc")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.info("REST request to delete music genre with ID: {}", id);
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lấy thông tin thể loại theo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin thể loại theo ID")
    public ResponseEntity<MusicGenreDTO> getGenreById(@PathVariable Long id) {
        log.info("REST request to get music genre with ID: {}", id);
        MusicGenreDTO result = genreService.getGenreById(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thông tin thể loại theo mã
     */
    @GetMapping("/code/{genreCode}")
    @Operation(summary = "Lấy thông tin thể loại theo mã")
    public ResponseEntity<MusicGenreDTO> getGenreByCode(@PathVariable String genreCode) {
        log.info("REST request to get music genre with code: {}", genreCode);
        MusicGenreDTO result = genreService.getGenreByCode(genreCode);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách tất cả thể loại
     */
    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả thể loại nhạc")
    public ResponseEntity<List<MusicGenreDTO>> getAllGenres() {
        log.info("REST request to get all music genres");
        List<MusicGenreDTO> result = genreService.getAllGenres();
        return ResponseEntity.ok(result);
    }
}

