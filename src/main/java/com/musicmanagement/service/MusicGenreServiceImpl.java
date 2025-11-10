package com.musicmanagement.service;

import com.musicmanagement.dto.MusicGenreDTO;
import com.musicmanagement.entity.MusicGenre;
import com.musicmanagement.exception.ResourceNotFoundException;
import com.musicmanagement.repository.MusicGenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation cho MusicGenre
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MusicGenreServiceImpl implements MusicGenreService {

    private final MusicGenreRepository genreRepository;

    @Override
    public MusicGenreDTO createGenre(MusicGenreDTO genreDTO) {
        log.info("Creating new music genre with code: {}", genreDTO.getGenreCode());

        // Kiểm tra trùng mã
        if (genreRepository.existsByGenreCode(genreDTO.getGenreCode())) {
            throw new IllegalArgumentException("Genre code already exists: " + genreDTO.getGenreCode());
        }

        MusicGenre genre = convertToEntity(genreDTO);
        MusicGenre savedGenre = genreRepository.save(genre);

        log.info("Created music genre with ID: {}", savedGenre.getId());
        return convertToDTO(savedGenre);
    }

    @Override
    public MusicGenreDTO updateGenre(Long id, MusicGenreDTO genreDTO) {
        log.info("Updating music genre with ID: {}", id);

        MusicGenre existingGenre = genreRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));

        // Kiểm tra trùng mã (nếu thay đổi mã)
        if (!existingGenre.getGenreCode().equals(genreDTO.getGenreCode())
            && genreRepository.existsByGenreCode(genreDTO.getGenreCode())) {
            throw new IllegalArgumentException("Genre code already exists: " + genreDTO.getGenreCode());
        }

        existingGenre.setGenreCode(genreDTO.getGenreCode());
        existingGenre.setGenreName(genreDTO.getGenreName());
        existingGenre.setDescription(genreDTO.getDescription());
        existingGenre.setAgeRange(genreDTO.getAgeRange()); // ✅ Thêm mapping cho ageRange

        MusicGenre updatedGenre = genreRepository.save(existingGenre);

        log.info("Updated music genre with ID: {}", id);
        return convertToDTO(updatedGenre);
    }

    @Override
    public void deleteGenre(Long id) {
        log.info("Deleting music genre with ID: {}", id);

        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre not found with id: " + id);
        }

        genreRepository.deleteById(id);
        log.info("Deleted music genre with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public MusicGenreDTO getGenreById(Long id) {
        log.debug("Fetching music genre with ID: {}", id);

        MusicGenre genre = genreRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));

        return convertToDTO(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public MusicGenreDTO getGenreByCode(String genreCode) {
        log.debug("Fetching music genre with code: {}", genreCode);

        MusicGenre genre = genreRepository.findByGenreCode(genreCode)
            .orElseThrow(() -> new ResourceNotFoundException("Genre not found with code: " + genreCode));

        return convertToDTO(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MusicGenreDTO> getAllGenres() {
        log.debug("Fetching all music genres");

        return genreRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByGenreCode(String genreCode) {
        return genreRepository.existsByGenreCode(genreCode);
    }

    /**
     * Convert Entity to DTO
     */
    private MusicGenreDTO convertToDTO(MusicGenre genre) {
        MusicGenreDTO dto = new MusicGenreDTO();
        dto.setId(genre.getId());
        dto.setGenreCode(genre.getGenreCode());
        dto.setGenreName(genre.getGenreName());
        dto.setDescription(genre.getDescription());
        dto.setAgeRange(genre.getAgeRange()); // ✅ Thêm mapping cho ageRange
        dto.setCreatedAt(genre.getCreatedAt());
        dto.setUpdatedAt(genre.getUpdatedAt());
        dto.setTotalFiles(genre.getMusicFiles() != null ? genre.getMusicFiles().size() : 0);
        return dto;
    }

    /**
     * Convert DTO to Entity
     */
    private MusicGenre convertToEntity(MusicGenreDTO dto) {
        MusicGenre genre = new MusicGenre();
        genre.setGenreCode(dto.getGenreCode());
        genre.setGenreName(dto.getGenreName());
        genre.setDescription(dto.getDescription());
        genre.setAgeRange(dto.getAgeRange()); // ✅ Thêm mapping cho ageRange
        return genre;
    }
}

