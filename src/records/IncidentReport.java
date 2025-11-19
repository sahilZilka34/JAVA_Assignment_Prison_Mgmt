package records;

import enums.SecurityLevel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * IncidentReport - Record type for incident documentation
 * 
 * Demonstrates:
 * ✅ RECORD type with multiple fields
 * ✅ Record with enum field
 * ✅ Compact constructor with complex validation
 * ✅ Custom methods in records
 */
public record IncidentReport(
    String reportId,
    LocalDateTime timestamp,
    String description,
    String reportedBy,
    String[] involvedPrisoners,  // Array in record!
    SecurityLevel severity,
    boolean resolved
) {
    
    // ============================================
    // COMPACT CONSTRUCTOR - Complex Validation
    // ============================================
    
    /**
     * Compact constructor with validation and defensive copying
     */
    public IncidentReport {
        // Validation
        if (reportId == null || reportId.isBlank()) {
            throw new IllegalArgumentException("Report ID cannot be empty");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (reportedBy == null || reportedBy.isBlank()) {
            throw new IllegalArgumentException("Reporter cannot be empty");
        }
        if (severity == null) {
            severity = SecurityLevel.MEDIUM;  // Default
        }
        
        // Normalize strings
        reportId = reportId.trim().toUpperCase();
        reportedBy = reportedBy.trim();
        description = description.trim();
        
        // DEFENSIVE COPYING of array
        // Critical: If we don't copy the array, external code could modify it
        if (involvedPrisoners != null) {
            involvedPrisoners = involvedPrisoners.clone();
        } else {
            involvedPrisoners = new String[0];
        }
    }
    
    // ============================================
    // CUSTOM ACCESSOR - Defensive Copy for Array
    // ============================================
    
    /**
     * Override default accessor to return defensive copy of array
     * 
     * The auto-generated accessor returns the internal array,
     * but we want to return a copy for safety
     */
    @Override
    public String[] involvedPrisoners() {
        // Return a copy, not the original array
        return involvedPrisoners.clone();
    }
    
    // ============================================
    // BUSINESS LOGIC METHODS
    // ============================================
    
    /**
     * Get number of prisoners involved
     */
    public int getInvolvedCount() {
        return involvedPrisoners.length;
    }
    
    /**
     * Check if a specific prisoner is involved
     */
    public boolean involves(String prisonerId) {
        if (prisonerId == null) return false;
        
        for (String id : involvedPrisoners) {
            if (id.equalsIgnoreCase(prisonerId)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if incident is recent (within last 24 hours)
     */
    public boolean isRecent() {
        return timestamp.isAfter(LocalDateTime.now().minusHours(24));
    }
    
    /**
     * Check if incident is high priority
     */
    public boolean isHighPriority() {
        return severity == SecurityLevel.HIGH || severity == SecurityLevel.MAXIMUM;
    }
    
    /**
     * Check if incident requires follow-up
     */
    public boolean requiresFollowUp() {
        return !resolved && isHighPriority();
    }
    
    /**
     * Get formatted timestamp
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    /**
     * Get incident status
     */
    public String getStatus() {
        if (resolved) {
            return "RESOLVED";
        }
        if (isHighPriority()) {
            return "URGENT - UNRESOLVED";
        }
        return "PENDING";
    }
    
    /**
     * Generate summary report
     */
    public String generateSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== INCIDENT REPORT ===\n");
        summary.append("ID: ").append(reportId).append("\n");
        summary.append("Time: ").append(getFormattedTimestamp()).append("\n");
        summary.append("Severity: ").append(severity.name()).append("\n");
        summary.append("Status: ").append(getStatus()).append("\n");
        summary.append("Reported by: ").append(reportedBy).append("\n");
        summary.append("Involved prisoners: ").append(getInvolvedCount()).append("\n");
        
        if (involvedPrisoners.length > 0) {
            summary.append("IDs: ");
            for (int i = 0; i < involvedPrisoners.length; i++) {
                summary.append(involvedPrisoners[i]);
                if (i < involvedPrisoners.length - 1) {
                    summary.append(", ");
                }
            }
            summary.append("\n");
        }
        
        summary.append("Description: ").append(description).append("\n");
        
        return summary.toString();
    }
    
    /**
     * Custom toString
     */
    @Override
    public String toString() {
        return String.format("IncidentReport[id=%s, severity=%s, status=%s, involved=%d]",
                           reportId, severity.name(), getStatus(), getInvolvedCount());
    }
    
    // ============================================
    // STATIC FACTORY METHODS
    // ============================================
    
    /**
     * Create a new unresolved incident
     */
    public static IncidentReport createUnresolved(String reportId, String description,
                                                  String reportedBy, String[] prisonerIds,
                                                  SecurityLevel severity) {
        return new IncidentReport(reportId, LocalDateTime.now(), description,
                                reportedBy, prisonerIds, severity, false);
    }
    
    /**
     * Create a resolved incident
     */
    public static IncidentReport createResolved(String reportId, LocalDateTime timestamp,
                                               String description, String reportedBy,
                                               String[] prisonerIds, SecurityLevel severity) {
        return new IncidentReport(reportId, timestamp, description,
                                reportedBy, prisonerIds, severity, true);
    }
    
    /**
     * Create minor incident (low severity)
     */
    public static IncidentReport createMinorIncident(String reportId, String description,
                                                     String reportedBy, String prisonerId) {
        return new IncidentReport(reportId, LocalDateTime.now(), description,
                                reportedBy, new String[]{prisonerId},
                                SecurityLevel.LOW, false);
    }
    
    /**
     * Create major incident (high severity)
     */
    public static IncidentReport createMajorIncident(String reportId, String description,
                                                     String reportedBy, String... prisonerIds) {
        // VARARGS used here! Can pass multiple prisoner IDs
        return new IncidentReport(reportId, LocalDateTime.now(), description,
                                reportedBy, prisonerIds,
                                SecurityLevel.MAXIMUM, false);
    }
}

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ✅ RECORD type
// ✅ Record with enum field (SecurityLevel)
// ✅ Record with array field (String[])
// ✅ Compact constructor with validation
// ✅ DEFENSIVE COPYING in constructor and accessor
// ✅ Custom accessors (overriding auto-generated)
// ✅ Custom methods
// ✅ Static factory methods with VARARGS
// ✅ StringBuilder for complex strings
// ✅ Date API (LocalDateTime)
// ✅ Arrays usage

// ============================================
// KEY LEARNING: Records + Mutable Fields
// ============================================
//
// Records are immutable, but if they contain mutable fields (like arrays),
// we need defensive copying:
//
// 1. In constructor: Clone the input array
// 2. In accessor: Clone before returning
//
// This ensures true immutability!