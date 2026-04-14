package events;

import java.time.LocalDateTime;

/**
 * Transfer - Implementation of sealed interface
 * 
 * Demonstrates:
 * ** final class implementing sealed interface
 * ** Additional business logic
 */
public final record Transfer(
    LocalDateTime timestamp,
    String prisonerId,
    String prisonerName,
    String fromCell,
    String toCell,
    String reason
) implements PrisonEvent {
    
    // Compact constructor
    public Transfer {
        if (prisonerId == null || prisonerId.isBlank()) {
            throw new IllegalArgumentException("Prisoner ID required");
        }
        if (prisonerName == null || prisonerName.isBlank()) {
            throw new IllegalArgumentException("Prisoner name required");
        }
        if (fromCell == null || fromCell.isBlank()) {
            throw new IllegalArgumentException("From cell required");
        }
        if (toCell == null || toCell.isBlank()) {
            throw new IllegalArgumentException("To cell required");
        }
        
        prisonerId = prisonerId.trim().toUpperCase();
        prisonerName = prisonerName.trim();
        fromCell = fromCell.trim();
        toCell = toCell.trim();
        reason = (reason == null || reason.isBlank()) ? "Administrative transfer" : reason.trim();
    }
    
    @Override
    public String getEventType() {
        return "TRANSFER";
    }
    
    @Override
    public String getDescription() {
        return String.format("Prisoner %s (%s) transferred from %s to %s. Reason: %s",
                           prisonerName, prisonerId, fromCell, toCell, reason);
    }
    
    /**
     * Check if this is an upgrade (better cell)
     */
    public boolean isUpgrade() {
        return reason.toLowerCase().contains("upgrade") || 
               reason.toLowerCase().contains("good behavior");
    }
    
    /**
     * Check if this is a downgrade (disciplinary)
     */
    public boolean isDowngrade() {
        return reason.toLowerCase().contains("disciplinary") ||
               reason.toLowerCase().contains("violation");
    }
    
    @Override
    public String toString() {
        return String.format("Transfer[%s - %s: %s → %s]",
                           timestamp, prisonerName, fromCell, toCell);
    }

    
}