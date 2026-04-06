package com.streetLeague.backend.enums;

/**
 * Enum for match status throughout its lifecycle.
 * 
 * Defines the different states a match can be in:
 * - SCHEDULED: Match is scheduled but not started
 * - IN_PROGRESS: Match is currently being played
 * - COMPLETED: Match has finished and results are recorded
 * - CANCELLED: Match has been cancelled
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
public enum MatchStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
