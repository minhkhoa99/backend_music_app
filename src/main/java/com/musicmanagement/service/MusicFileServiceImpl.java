package com.musicmanagement.service;

import com.musicmanagement.dto.MusicFileDTO;
import com.musicmanagement.dto.ReportDTO;
import com.musicmanagement.entity.MusicFile;
import com.musicmanagement.entity.MusicGenre;
import com.musicmanagement.exception.ResourceNotFoundException;
import com.musicmanagement.repository.MusicFileRepository;
import com.musicmanagement.repository.MusicGenreRepository;
import com.musicmanagement.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation cho MusicFile
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MusicFileServiceImpl implements MusicFileService {

    private final MusicFileRepository musicFileRepository;
    private final MusicGenreRepository genreRepository;
    private final FileUtil fileUtil;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.thumbnail.dir}")
    private String thumbnailDir;

    @Override
    public MusicFileDTO createMusicFile(MusicFileDTO musicFileDTO) {
        log.info("Creating new music file with code: {}", musicFileDTO.getFileCode());

        if (musicFileRepository.existsByFileCode(musicFileDTO.getFileCode())) {
            throw new IllegalArgumentException("File code already exists: " + musicFileDTO.getFileCode());
        }

        MusicFile musicFile = convertToEntity(musicFileDTO);
        MusicFile savedFile = musicFileRepository.save(musicFile);

        log.info("Created music file with ID: {}", savedFile.getId());
        return convertToDTO(savedFile);
    }

    @Override
    public MusicFileDTO updateMusicFile(Long id, MusicFileDTO musicFileDTO) {
        log.info("Updating music file with ID: {}", id);

        MusicFile existingFile = musicFileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Music file not found with id: " + id));

        // Kiểm tra trùng mã
        if (!existingFile.getFileCode().equals(musicFileDTO.getFileCode())
            && musicFileRepository.existsByFileCode(musicFileDTO.getFileCode())) {
            throw new IllegalArgumentException("File code already exists: " + musicFileDTO.getFileCode());
        }

        updateEntityFromDTO(existingFile, musicFileDTO);
        MusicFile updatedFile = musicFileRepository.save(existingFile);

        log.info("Updated music file with ID: {}", id);
        return convertToDTO(updatedFile);
    }

    @Override
    public void deleteMusicFile(Long id) {
        log.info("Deleting music file with ID: {}", id);

        MusicFile musicFile = musicFileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Music file not found with id: " + id));

        // Xóa file vật lý nếu có
        try {
            if (musicFile.getFilePath() != null) {
                fileUtil.deleteFile(musicFile.getFilePath());
            }
            if (musicFile.getThumbnailPath() != null) {
                fileUtil.deleteFile(musicFile.getThumbnailPath());
            }
        } catch (IOException e) {
            log.warn("Failed to delete physical files for music file ID: {}", id, e);
        }

        musicFileRepository.deleteById(id);
        log.info("Deleted music file with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public MusicFileDTO getMusicFileById(Long id) {
        log.debug("Fetching music file with ID: {}", id);

        MusicFile musicFile = musicFileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Music file not found with id: " + id));

        return convertToDTO(musicFile);
    }

    @Override
    @Transactional(readOnly = true)
    public MusicFileDTO getMusicFileByCode(String fileCode) {
        log.debug("Fetching music file with code: {}", fileCode);

        MusicFile musicFile = musicFileRepository.findByFileCode(fileCode)
            .orElseThrow(() -> new ResourceNotFoundException("Music file not found with code: " + fileCode));

        return convertToDTO(musicFile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MusicFileDTO> getAllMusicFiles(Pageable pageable) {
        log.debug("Fetching all music files with pagination");

        return musicFileRepository.findAll(pageable)
            .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MusicFileDTO> searchMusicFiles(String keyword, Pageable pageable) {
        log.debug("Searching music files with keyword: {}", keyword);

        return musicFileRepository.searchByKeyword(keyword, pageable)
            .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MusicFileDTO> getMusicFilesByGenre(Long genreId, Pageable pageable) {
        log.debug("Fetching music files by genre ID: {}", genreId);

        return musicFileRepository.findByGenreId(genreId, pageable)
            .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MusicFileDTO> getMusicFilesByFileType(String fileType, Pageable pageable) {
        log.debug("Fetching music files by file type: {}", fileType);

        return musicFileRepository.findByFileType(fileType, pageable)
            .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MusicFileDTO> getMusicFilesByArtist(String artist, Pageable pageable) {
        log.debug("Fetching music files by artist: {}", artist);

        return musicFileRepository.findByArtistContainingIgnoreCase(artist, pageable)
            .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MusicFileDTO> getMusicFilesByReleaseYear(Integer year, Pageable pageable) {
        log.debug("Fetching music files by release year: {}", year);

        return musicFileRepository.findByReleaseYear(year, pageable)
            .map(this::convertToDTO);
    }

    @Override
    public MusicFileDTO uploadMusicFile(MultipartFile file, MusicFileDTO musicFileDTO) {
        log.info("Uploading music file: {}", file.getOriginalFilename());

        try {
            // Validate file
            fileUtil.validateMusicFile(file);

            // Save file
            String filePath = fileUtil.saveMusicFile(file, uploadDir);

            // Extract metadata
            var metadata = fileUtil.extractMetadata(filePath);

            // Set file info
            musicFileDTO.setFilePath(filePath);
            musicFileDTO.setFileType(fileUtil.getFileExtension(file.getOriginalFilename()));
            musicFileDTO.setFileSize(file.getSize());

            // Set metadata if not provided
            if (musicFileDTO.getDuration() == null && metadata.get("duration") != null) {
                musicFileDTO.setDuration((Integer) metadata.get("duration"));
            }
            if (musicFileDTO.getArtist() == null && metadata.get("artist") != null) {
                musicFileDTO.setArtist((String) metadata.get("artist"));
            }
            if (musicFileDTO.getAlbum() == null && metadata.get("album") != null) {
                musicFileDTO.setAlbum((String) metadata.get("album"));
            }

            return createMusicFile(musicFileDTO);

        } catch (IOException e) {
            log.error("Failed to upload music file", e);
            throw new RuntimeException("Failed to upload music file: " + e.getMessage());
        }
    }

    @Override
    public String uploadThumbnail(Long musicFileId, MultipartFile thumbnail) {
        log.info("Uploading thumbnail for music file ID: {}", musicFileId);

        MusicFile musicFile = musicFileRepository.findById(musicFileId)
            .orElseThrow(() -> new ResourceNotFoundException("Music file not found with id: " + musicFileId));

        try {
            // Delete old thumbnail if exists
            if (musicFile.getThumbnailPath() != null) {
                fileUtil.deleteFile(musicFile.getThumbnailPath());
            }

            // Save new thumbnail
            String thumbnailPath = fileUtil.saveImage(thumbnail, thumbnailDir);
            musicFile.setThumbnailPath(thumbnailPath);
            musicFileRepository.save(musicFile);

            log.info("Uploaded thumbnail for music file ID: {}", musicFileId);
            return thumbnailPath;

        } catch (IOException e) {
            log.error("Failed to upload thumbnail", e);
            throw new RuntimeException("Failed to upload thumbnail: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MusicFileDTO> getOldMusicFiles(int minAge) {
        log.debug("Fetching music files older than {} years", minAge);

        int currentYear = LocalDateTime.now().getYear();
        int maxReleaseYear = currentYear - minAge;

        return musicFileRepository.findOldMusicFiles(maxReleaseYear).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportByGenre() {
        log.debug("Generating report by genre");

        List<Object[]> results = musicFileRepository.countByGenre();
        return results.stream()
            .map(row -> new ReportDTO((String) row[0], (Long) row[1], null))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportByYear() {
        log.debug("Generating report by year");

        List<Object[]> results = musicFileRepository.countByReleaseYear();
        return results.stream()
            .map(row -> new ReportDTO((Integer) row[0], (Long) row[1]))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getStorageReport() {
        log.debug("Generating storage report");

        Long totalFiles = musicFileRepository.count();
        Long totalSize = musicFileRepository.getTotalFileSize();

        ReportDTO report = new ReportDTO();
        report.setTotalFiles(totalFiles);
        report.setTotalStorageSize(totalSize != null ? totalSize : 0L);
        report.setFormattedStorageSize(fileUtil.formatFileSize(totalSize != null ? totalSize : 0L));

        return report;
    }

    /**
     * Convert Entity to DTO
     */
    private MusicFileDTO convertToDTO(MusicFile musicFile) {
        MusicFileDTO dto = new MusicFileDTO();
        dto.setId(musicFile.getId());
        dto.setFileCode(musicFile.getFileCode());
        dto.setFileName(musicFile.getFileName());
        dto.setFilePath(musicFile.getFilePath());
        dto.setThumbnailPath(musicFile.getThumbnailPath());
        dto.setFileType(musicFile.getFileType());
        dto.setDownloadLink(musicFile.getDownloadLink());
        dto.setDescription(musicFile.getDescription());
        dto.setArtist(musicFile.getArtist());
        dto.setAlbum(musicFile.getAlbum());
        dto.setDuration(musicFile.getDuration());
        dto.setFileSize(musicFile.getFileSize());
        dto.setReleaseYear(musicFile.getReleaseYear());
        dto.setCreatedAt(musicFile.getCreatedAt());
        dto.setUpdatedAt(musicFile.getUpdatedAt());
        dto.setAge(musicFile.getAge());

        if (musicFile.getGenre() != null) {
            dto.setGenreId(musicFile.getGenre().getId());
            dto.setGenreName(musicFile.getGenre().getGenreName());
        }

        return dto;
    }

    /**
     * Convert DTO to Entity
     */
    private MusicFile convertToEntity(MusicFileDTO dto) {
        MusicFile musicFile = new MusicFile();
        updateEntityFromDTO(musicFile, dto);
        return musicFile;
    }

    /**
     * Update entity from DTO
     */
    private void updateEntityFromDTO(MusicFile musicFile, MusicFileDTO dto) {
        musicFile.setFileCode(dto.getFileCode());
        musicFile.setFileName(dto.getFileName());

        if (dto.getFilePath() != null) {
            musicFile.setFilePath(dto.getFilePath());
        }
        if (dto.getThumbnailPath() != null) {
            musicFile.setThumbnailPath(dto.getThumbnailPath());
        }

        musicFile.setFileType(dto.getFileType());
        musicFile.setDownloadLink(dto.getDownloadLink());
        musicFile.setDescription(dto.getDescription());
        musicFile.setArtist(dto.getArtist());
        musicFile.setAlbum(dto.getAlbum());
        musicFile.setDuration(dto.getDuration());
        musicFile.setFileSize(dto.getFileSize());
        musicFile.setReleaseYear(dto.getReleaseYear());

        if (dto.getGenreId() != null) {
            MusicGenre genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + dto.getGenreId()));
            musicFile.setGenre(genre);
        }
    }
}

