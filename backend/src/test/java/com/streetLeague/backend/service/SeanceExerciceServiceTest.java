package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.SeanceExerciceDTO;
import com.streetLeague.backend.entity.Exercice;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.SeanceExercice;
import com.streetLeague.backend.enums.TypeExercice;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.mapper.ExerciceMapper;
import com.streetLeague.backend.repository.ExerciceRepository;
import com.streetLeague.backend.repository.SeanceExerciceRepository;
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
class SeanceExerciceServiceTest {

    @Mock
    private SeanceExerciceRepository seanceExerciceRepository;
    @Mock
    private SeanceEntrainementService seanceService;
    @Mock
    private ExerciceRepository exerciceRepository;

    @InjectMocks
    private SeanceExerciceService seanceExerciceService;

    private SeanceEntrainement seance;
    private Exercice exercice;
    private SeanceExercice seanceExercice;

    @BeforeEach
    void setUp() {
        seance = SeanceEntrainement.builder().idSeance(1).titreSeance("Séance Force").build();
        exercice = Exercice.builder().idExercice(1).nom("Pompes").type(TypeExercice.FORCE).build();
        seanceExercice = SeanceExercice.builder()
                .idSeanceExercice(1)
                .seance(seance)
                .exercice(exercice)
                .series(3)
                .repetitions(12)
                .charge(0f)
                .ordre(1)
                .build();
    }

    @Test
    void create_shouldSucceed_withValidData() {
        SeanceExerciceDTO.Request dto = new SeanceExerciceDTO.Request();
        dto.setSeanceId(1);
        dto.setExerciceId(1);
        dto.setSeries(3);
        dto.setRepetitions(12);
        dto.setOrdre(1);

        when(seanceService.findOrThrow(1)).thenReturn(seance);
        when(exerciceRepository.findById(1)).thenReturn(Optional.of(exercice));
        when(seanceExerciceRepository.save(any())).thenReturn(seanceExercice);

        SeanceExerciceDTO.Response result = seanceExerciceService.create(dto);

        assertNotNull(result);
        verify(seanceExerciceRepository, times(1)).save(any());
    }

    @Test
    void create_shouldThrow_whenNoRepetitionsAndNoTime() {
        SeanceExerciceDTO.Request dto = new SeanceExerciceDTO.Request();
        dto.setSeanceId(1);
        dto.setExerciceId(1);
        dto.setSeries(3);
        dto.setRepetitions(0);
        dto.setTempsSecondes(0);
        dto.setOrdre(1);

        when(seanceService.findOrThrow(1)).thenReturn(seance);
        when(exerciceRepository.findById(1)).thenReturn(Optional.of(exercice));

        assertThrows(BusinessRuleException.class, () -> seanceExerciceService.create(dto));
    }

    @Test
    void create_shouldSucceed_withTimeOnly() {
        SeanceExerciceDTO.Request dto = new SeanceExerciceDTO.Request();
        dto.setSeanceId(1);
        dto.setExerciceId(1);
        dto.setSeries(1);
        dto.setRepetitions(0);
        dto.setTempsSecondes(60);
        dto.setOrdre(1);

        when(seanceService.findOrThrow(1)).thenReturn(seance);
        when(exerciceRepository.findById(1)).thenReturn(Optional.of(exercice));
        when(seanceExerciceRepository.save(any())).thenReturn(seanceExercice);

        SeanceExerciceDTO.Response result = seanceExerciceService.create(dto);

        assertNotNull(result);
    }

    @Test
    void getBySeance_shouldReturnOrderedList() {
        SeanceExercice se2 = SeanceExercice.builder()
                .idSeanceExercice(2).seance(seance).exercice(exercice).series(4).repetitions(8).ordre(2).build();

        when(seanceExerciceRepository.findBySeanceIdSeanceOrderByOrdreAsc(1))
                .thenReturn(Arrays.asList(seanceExercice, se2));

        List<SeanceExerciceDTO.Response> result = seanceExerciceService.getBySeance(1);

        assertEquals(2, result.size());
    }

    @Test
    void delete_shouldCallRepository() {
        when(seanceExerciceRepository.findById(1)).thenReturn(Optional.of(seanceExercice));

        seanceExerciceService.delete(1);

        verify(seanceExerciceRepository, times(1)).delete(seanceExercice);
    }
}
