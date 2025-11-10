package com.musicmanagement.service;

import com.musicmanagement.dto.MusicGenreDTO;
import com.musicmanagement.entity.MusicGenre;
import com.musicmanagement.exception.ResourceNotFoundException;
import com.musicmanagement.repository.MusicGenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests cho MusicGenreService
 */
@ExtendWith(MockitoExtension.class)
class MusicGenreServiceImplTest {

    @Mock
    private MusicGenreRepository genreRepository;

    @InjectMocks
    private MusicGenreServiceImpl genreService;

    private MusicGenre testGenre;
    private MusicGenreDTO testGenreDTO;

    @BeforeEach
    void setUp() {
        testGenre = new MusicGenre();
        testGenre.setId(1L);
        testGenre.setGenreCode("ROCK");
        testGenre.setGenreName("Rock");
        testGenre.setDescription("Rock music");

        testGenreDTO = new MusicGenreDTO();
        testGenreDTO.setGenreCode("ROCK");
        testGenreDTO.setGenreName("Rock");
        testGenreDTO.setDescription("Rock music");
    }

    @Test
    void testCreateGenre_Success() {
        when(genreRepository.existsByGenreCode(anyString())).thenReturn(false);
        when(genreRepository.save(any(MusicGenre.class))).thenReturn(testGenre);

        MusicGenreDTO result = genreService.createGenre(testGenreDTO);

        assertNotNull(result);
        assertEquals("ROCK", result.getGenreCode());
        verify(genreRepository, times(1)).save(any(MusicGenre.class));
    }

    @Test
    void testCreateGenre_DuplicateCode() {
        when(genreRepository.existsByGenreCode("ROCK")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            genreService.createGenre(testGenreDTO);
        });

        verify(genreRepository, never()).save(any(MusicGenre.class));
    }

    @Test
    void testGetGenreById_Success() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(testGenre));

        MusicGenreDTO result = genreService.getGenreById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ROCK", result.getGenreCode());
    }

    @Test
    void testGetGenreById_NotFound() {
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            genreService.getGenreById(999L);
        });
    }

    @Test
    void testGetAllGenres() {
        MusicGenre genre2 = new MusicGenre();
        genre2.setId(2L);
        genre2.setGenreCode("POP");
        genre2.setGenreName("Pop");

        when(genreRepository.findAll()).thenReturn(Arrays.asList(testGenre, genre2));

        List<MusicGenreDTO> result = genreService.getAllGenres();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateGenre_Success() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(testGenre));
        when(genreRepository.existsByGenreCode("ROCK")).thenReturn(false);
        when(genreRepository.save(any(MusicGenre.class))).thenReturn(testGenre);

        MusicGenreDTO updatedDTO = new MusicGenreDTO();
        updatedDTO.setGenreCode("ROCK");
        updatedDTO.setGenreName("Rock Music Updated");

        MusicGenreDTO result = genreService.updateGenre(1L, updatedDTO);

        assertNotNull(result);
        verify(genreRepository, times(1)).save(any(MusicGenre.class));
    }

    @Test
    void testDeleteGenre_Success() {
        when(genreRepository.existsById(1L)).thenReturn(true);
        doNothing().when(genreRepository).deleteById(1L);

        genreService.deleteGenre(1L);

        verify(genreRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteGenre_NotFound() {
        when(genreRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            genreService.deleteGenre(999L);
        });

        verify(genreRepository, never()).deleteById(anyLong());
    }
}

