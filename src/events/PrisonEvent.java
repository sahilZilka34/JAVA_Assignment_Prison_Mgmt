package events;

import java.time.LocalDateTime;

/**
 * PrisonEvent - SEALED INTERFACE (Java 17+)
 * 
 * Demonstrates:
 * ** SEALED interface (advanced requirement!)
 * ** Restricted inheritance hierarchy
 * ** Pattern matching with sealed types
 */
public sealed interface PrisonEvent permits Admission, Release, Transfer {
    
    /**
     * Get event timestamp
     * All events must have a timestamp
     */
    LocalDateTime timestamp();
    
    /**
     * Get prisoner ID involved in the event
     * All events involve a prisoner
     */
    String prisonerId();
    
    /**
     * Get event type as string
     */
    String getEventType();
    
    /**
     * Get event description
     */
    String getDescription();
    
    /**
     * Check if event is recent (within last 24 hours)
     */
    default boolean isRecent() {
        return timestamp().isAfter(LocalDateTime.now().minusHours(24));
    }
    
    /**
     * Get formatted timestamp
     */
    default String getFormattedTimestamp() {
        return timestamp().toString();
    }
}