package events;

import java.time.LocalDateTime;

/**
 * Admission - Implementation of sealed interface
 * 
 * Demonstrates:
 * ** final class (implements sealed interface)
 * ** Record implementing interface
 */
public final record Admission(
    LocalDateTime timestamp,
    String prisonerId,
    String prisonerName,
    String cellNumber,
    String reason
) implements PrisonEvent {
    
    // Compact constructor for validation
    public Admission {
        if (prisonerId == null || prisonerId.isBlank()) {
            throw new IllegalArgumentException("Prisoner ID required");
        }
        if (prisonerName == null || prisonerName.isBlank()) {
            throw new IllegalArgumentException("Prisoner name required");
        }
        
        prisonerId = prisonerId.trim().toUpperCase();
        prisonerName = prisonerName.trim();
        cellNumber = (cellNumber == null) ? "Unassigned" : cellNumber.trim();
        reason = (reason == null || reason.isBlank()) ? "Standard admission" : reason.trim();
    }
    
    @Override
    public String getEventType() {
        return "ADMISSION";
    }
    
    @Override
    public String getDescription() {
        return String.format("Prisoner %s (%s) admitted to cell %s. Reason: %s",
                           prisonerName, prisonerId, cellNumber, reason);
    }
    
    @Override
    public String toString() {
        return String.format("Admission[%s - %s to %s]", 
                           timestamp, prisonerName, cellNumber);
    }

    @Override
    public LocalDateTime getTimestamp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Override
    public String getPrisonerId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPrisonerId'");
    }
}