package records;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * VisitRecord - Record type for storing visit information
 * 
 * Demonstrates:
 * ** RECORD type (Java 14+, LTS in Java 17)
 * ** Immutable data carrier
 * ** Auto-generated constructor, getters, equals, hashCode, toString
 * ** Compact constructor for validation
 * 
 * What is a Record?
 * - Special class for holding immutable data
 * - Automatically generates: constructor, getters, equals(), hashCode(), toString()
 * - All fields are final (immutable)
 * - Cannot extend other classes (but can implement interfaces)
 * - Perfect for DTOs (Data Transfer Objects)
 * 
 * Why use Records?
 * - Less boilerplate code (no need to write getters, equals, etc.)
 * - Immutability by default (thread-safe)
 * - Clear intent: "This is just data"
 * - Better than regular classes for simple data holders
 * 
 * Syntax: record Name(fields) { }
 */
public record VisitRecord(
    String visitorName,
    String prisonerId,
    LocalDateTime visitDateTime,
    int durationMinutes,
    String purpose,
    boolean approved
) {
    
    // ============================================
    // COMPACT CONSTRUCTOR - Validation
    // ============================================
    
    /**
     * Compact constructor - special syntax for records
     * Used for validation and normalization
     * 
     * Note: We don't write "this.field = field" - that's automatic!
     * We just add validation logic here
     */
    public VisitRecord {
        // Validation (compact constructor style)
        if (visitorName == null || visitorName.isBlank()) {
            throw new IllegalArgumentException("Visitor name cannot be empty");
        }
        if (prisonerId == null || prisonerId.isBlank()) {
            throw new IllegalArgumentException("Prisoner ID cannot be empty");
        }
        if (visitDateTime == null) {
            throw new IllegalArgumentException("Visit date/time cannot be null");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        
        // Normalize data
        visitorName = visitorName.trim();
        prisonerId = prisonerId.trim().toUpperCase();
        purpose = (purpose == null || purpose.isBlank()) ? "General visit" : purpose.trim();
    }
    
    // ============================================
    // ADDITIONAL METHODS (Custom logic in records)
    // ============================================
    
    /**
     * Calculate visit end time
     * Records CAN have methods beyond the auto-generated ones
     */
    public LocalDateTime calculateEndTime() {
        return visitDateTime.plusMinutes(durationMinutes);
    }
    
    /**
     * Check if visit is in the past
     */
    public boolean isPastVisit() {
        return visitDateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Check if visit is upcoming (future)
     */
    public boolean isUpcoming() {
        return visitDateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Check if visit is currently in progress
     */
    public boolean isInProgress() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = calculateEndTime();
        return visitDateTime.isBefore(now) && endTime.isAfter(now);
    }
    
    /**
     * Get formatted date/time for display
     */
    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return visitDateTime.format(formatter);
    }
    
    /**
     * Get visit status
     */
    public String getStatus() {
        if (!approved) {
            return "DENIED";
        }
        if (isInProgress()) {
            return "IN PROGRESS";
        }
        if (isPastVisit()) {
            return "COMPLETED";
        }
        return "SCHEDULED";
    }
    
    /**
     * Custom toString with better formatting
     * (Overrides the auto-generated one)
     */
    @Override
    public String toString() {
        return String.format("VisitRecord[visitor=%s, prisoner=%s, date=%s, duration=%d min, status=%s]",
                           visitorName, prisonerId, getFormattedDateTime(), 
                           durationMinutes, getStatus());
    }
    
    // ============================================
    // STATIC FACTORY METHODS (Common pattern with records)
    // ============================================
    
    /**
     * Factory method for creating approved visits
     */
    public static VisitRecord createApprovedVisit(String visitorName, String prisonerId,
                                                  LocalDateTime dateTime, int duration,
                                                  String purpose) {
        return new VisitRecord(visitorName, prisonerId, dateTime, duration, purpose, true);
    }
    
    /**
     * Factory method for creating denied visits
     */
    public static VisitRecord createDeniedVisit(String visitorName, String prisonerId,
                                                LocalDateTime dateTime, String reason) {
        return new VisitRecord(visitorName, prisonerId, dateTime, 0, 
                             "DENIED: " + reason, false);
    }
    
    /**
     * Factory method for standard 1-hour visit
     */
    public static VisitRecord createStandardVisit(String visitorName, String prisonerId,
                                                  LocalDateTime dateTime) {
        return new VisitRecord(visitorName, prisonerId, dateTime, 60, 
                             "General visit", true);
    }
}

// ============================================
// KEY RECORD FEATURES DEMONSTRATED:
// ============================================
// ** RECORD type (Java 14+)
// ** Immutable data (all fields final)
// ** Compact constructor (validation)
// ** Auto-generated: constructor, getters, equals, hashCode, toString
// ** Custom methods (calculateEndTime, isInProgress, etc.)
// ** Overriding auto-generated methods (toString)
// ** Static factory methods
// ** Date API (LocalDateTime)
// ** String formatting

// ============================================
// COMPARISON: Record vs Regular Class
// ============================================
//
// WITH RECORD (6 lines):
// record Person(String name, int age) { }
//
// WITHOUT RECORD (40+ lines):
// public class Person {
//     private final String name;
//     private final int age;
//     
//     public Person(String name, int age) {
//         this.name = name;
//         this.age = age;
//     }
//     
//     public String name() { return name; }
//     public int age() { return age; }
//     
//     @Override
//     public boolean equals(Object obj) { ... }
//     @Override
//     public int hashCode() { ... }
//     @Override
//     public String toString() { ... }
// }
//
// Records save TONS of boilerplate!

// ============================================
// USAGE EXAMPLES:
// ============================================
//
// // Create record
// var visit = new VisitRecord(
//     "John Smith",
//     "P-2024-001",
//     LocalDateTime.of(2024, 10, 20, 14, 0),
//     60,
//     "Family visit",
//     true
// );
//
// // Access data (auto-generated getters)
// String name = visit.visitorName();
// LocalDateTime time = visit.visitDateTime();
// boolean approved = visit.approved();
//
// // Use custom methods
// LocalDateTime endTime = visit.calculateEndTime();
// boolean inProgress = visit.isInProgress();
//
// // Records are immutable - cannot change fields
// // visit.visitorName = "New Name";  // Won't compile!
//
// // But you can create a new record with modified values
// var modified = new VisitRecord(
//     "Jane Doe",  // changed
//     visit.prisonerId(),  // same
//     visit.visitDateTime(),  // same
//     visit.durationMinutes(),  // same
//     visit.purpose(),  // same
//     visit.approved()  // same
// );