package events;

import java.time.LocalDateTime;

/**
 * Release - Implementation of sealed interface
 * 
 * Demonstrates:
 * ** final class implementing sealed interface
 * ** Business logic in event
 */
public final record Release(
    LocalDateTime timestamp,
    String prisonerId,
    String prisonerName,
    String releaseType,  // "SENTENCE_COMPLETE", "PAROLE", "TRANSFER_OUT"
    String destination
) implements PrisonEvent {
    
    // Compact constructor
    public Release {
        if (prisonerId == null || prisonerId.isBlank()) {
            throw new IllegalArgumentException("Prisoner ID required");
        }
        if (prisonerName == null || prisonerName.isBlank()) {
            throw new IllegalArgumentException("Prisoner name required");
        }
        
        prisonerId = prisonerId.trim().toUpperCase();
        prisonerName = prisonerName.trim();
        releaseType = (releaseType == null) ? "STANDARD" : releaseType.trim().toUpperCase();
        destination = (destination == null || destination.isBlank()) ? "N/A" : destination.trim();
    }
    
    @Override
    public String getEventType() {
        return "RELEASE";
    }
    
    @Override
    public String getDescription() {
        return String.format("Prisoner %s (%s) released. Type: %s, Destination: %s",
                           prisonerName, prisonerId, releaseType, destination);
    }
    
    /**
     * Check if this is an early release (parole)
     */
    public boolean isEarlyRelease() {
        return releaseType.contains("PAROLE");
    }
    
    /**
     * Check if prisoner was transferred to another facility
     */
    public boolean isTransferOut() {
        return releaseType.contains("TRANSFER");
    }
    
    @Override
    public String toString() {
        return String.format("Release[%s - %s via %s]",
                           timestamp, prisonerName, releaseType);
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