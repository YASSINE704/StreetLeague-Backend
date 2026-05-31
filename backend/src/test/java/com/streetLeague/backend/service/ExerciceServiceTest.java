package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.ExerciceDTO;
import com.streetLeague.backend.entity.Exercice;
import com.streetLeague.backend.enums.TypeExercice;
import com.streetLeague.backend.mapper.ExerciceMapper;
import com.streetLeague.backend.repository.ExerciceRepository;
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

@ExtendWith(MockitoExtension.class)
class ExerciceServiceTest {

    @Mock
    private ExerciceRepository exerciceRepository;

    @InjectMocks
    private ExerciceService exerciceService;

    private Exercice exercice;

    @BeforeEach
    void setUp() {
        exercice = Exercice.builder()
                .idExercice(1)
                .nom("Pompes explosives")
                .description("Renforcement pectoraux")
                .type(TypeExercice.FORCE)
                .build();
    }

    @Test
    void getAll_shouldReturnAllExercices() {
        Exercice ex2 = Exercice.builder().idExercice(2).nom("Squats").type(TypeExercice.FORCE).build();
        when(exerciceRepository.findAll()).thenReturn(Arrays.asList(exercice, ex2));

        List<ExerciceDTO.Response> result = exerciceService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(exerciceRepository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnExercice_whenExists() {
        when(exerciceRepository.findById(1)).thenReturn(Optional.of(exercice));

        ExerciceDTO.Response result = exerciceService.getById(1);

        assertNotNull(result);
        assertEquals("Pompes explosives", result.getNom());
        assertEquals(TypeExercice.FORCE, result.getType());
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(exerciceRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> exerciceService.getById(99));
    }

    @Test
    void getByType_shouldReturnFilteredExercices() {
        when(exerciceRepository.findByType(TypeExercice.FORCE)).thenReturn(Arrays.asList(exercice));

        List<ExerciceDTO.Response> result = exerciceService.getByType(TypeExercice.FORCE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TypeExercice.FORCE, result.get(0).getType());
    }

    @Test
    void delete_shouldCallRepository() {
        when(exerciceRepository.findById(1)).thenReturn(Optional.of(exercice));
        doNothing().when(exerciceRepository).deleteById(1);

        exerciceService.delete(1);

        verify(exerciceRepository, times(1)).deleteById(1);
    }
}
