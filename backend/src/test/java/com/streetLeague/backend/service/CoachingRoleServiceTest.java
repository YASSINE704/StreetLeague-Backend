package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoachingRoleServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CoachingRoleService coachingRoleService;

    @Test
    void requireCoachOrAdmin_shouldPass_whenUserIsCoach() {
        User coach = new User();
        coach.setIdUser(1);
        coach.setRole(Role.COACH);
        when(userRepository.findById(1)).thenReturn(Optional.of(coach));

        User result = coachingRoleService.requireCoachOrAdmin(1);

        assertNotNull(result);
        assertEquals(Role.COACH, result.getRole());
    }

    @Test
    void requireCoachOrAdmin_shouldPass_whenUserIsAdmin() {
        User admin = new User();
        admin.setIdUser(2);
        admin.setRole(Role.ADMIN);
        when(userRepository.findById(2)).thenReturn(Optional.of(admin));

        User result = coachingRoleService.requireCoachOrAdmin(2);

        assertNotNull(result);
        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    void requireCoachOrAdmin_shouldThrow_whenUserIsSportif() {
        User sportif = new User();
        sportif.setIdUser(3);
        sportif.setRole(Role.SPORTIF);
        when(userRepository.findById(3)).thenReturn(Optional.of(sportif));

        assertThrows(BusinessRuleException.class, () -> coachingRoleService.requireCoachOrAdmin(3));
    }

    @Test
    void requireSportifOrCoachOrAdmin_shouldPass_whenUserIsSportif() {
        User sportif = new User();
        sportif.setIdUser(4);
        sportif.setRole(Role.SPORTIF);
        when(userRepository.findById(4)).thenReturn(Optional.of(sportif));

        User result = coachingRoleService.requireSportifOrCoachOrAdmin(4);

        assertNotNull(result);
        assertEquals(Role.SPORTIF, result.getRole());
    }

    @Test
    void findUserOrThrow_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> coachingRoleService.findUserOrThrow(99));
    }
}
