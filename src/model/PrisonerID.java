package model;

import java.time.LocalDate;
import java.time.Year;
import java.util.Objects;

/**
 * PrisonerID - Custom Immutable Type
 * 
 * Demonstrates:
 * ** CUSTOM IMMUTABLE TYPE (advanced requirement)
 * ** All fields are final
 * ** No setters
 * ** Defensive copying where needed
 * ** Thread-safe by design
 * 
 * What is an Immutable Type?
 * - Object whose state cannot be changed after creation
 * - All fields are final
 * - No setter methods
 * - Any mutable fields are defensively copied
 * - Thread-safe without synchronization
 * 
 * Why Create Immutable Types?
 * - Thread safety (multiple threads can share safely)
 * - Simpler reasoning (state never changes)
 * - Good for keys in HashMaps/Sets
 * - Prevents bugs from unexpected mutations
 * 
 * How to Make a Class Immutable:
 * 1. Make class final (prevent subclassing)
 * 2. Make all fields private and final
 * 3. Don't provide setters
 * 4. Defensive copy mutable objects in constructor
 * 5. Defensive copy mutable objects in getters
 * 6. Make constructor validate input
 */
public final class PrisonerID {  // final class - cannot be extended
    
    // All fields are FINAL - cannot be changed after construction
    private final String prefix;           // "P"
    private final int year;                // 2024
    private final int sequenceNumber;      // 001
    private final LocalDate issueDate;     // When ID was issued
    
    // ============================================
    // CONSTRUCTORS - The ONLY way to set values
    // ============================================
    
    /**
     * Primary constructor with all fields
     * 
     * @param prefix ID prefix (e.g., "P" for prisoner)
     * @param year Year of admission
     * @param sequenceNumber Unique sequence number
     * @param issueDate Date when ID was issued
     */
    public PrisonerID(String prefix, int year, int sequenceNumber, LocalDate issueDate) {
        // Validation
        if (prefix == null || prefix.isBlank()) {
            throw new IllegalArgumentException("Prefix cannot be empty");
        }
        if (year < 1900 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        if (sequenceNumber < 1) {
            throw new IllegalArgumentException("Sequence number must be positive");
        }
        if (issueDate == null) {
            throw new IllegalArgumentException("Issue date cannot be null");
        }
        
        // Initialize final fields (can only be done ONCE)
        this.prefix = prefix.trim().toUpperCase();
        this.year = year;
        this.sequenceNumber = sequenceNumber;
        // LocalDate is immutable, so no need for defensive copy
        this.issueDate = issueDate;
    }
    
    /**
     * Constructor with current date as issue date
     */
    public PrisonerID(String prefix, int year, int sequenceNumber) {
        this(prefix, year, sequenceNumber, LocalDate.now());
    }
    
    /**
     * Constructor with current year
     */
    public PrisonerID(String prefix, int sequenceNumber) {
        this(prefix, Year.now().getValue(), sequenceNumber, LocalDate.now());
    }
    
    // ============================================
    // GETTERS ONLY - No setters! (Immutable!)
    // ============================================
    
    /**
     * Get prefix
     * String is immutable, so safe to return directly
     */
    public String getPrefix() {
        return prefix;
    }
    
    /**
     * Get year
     * Primitives are copied by value, so safe
     */
    public int getYear() {
        return year;
    }
    
    /**
     * Get sequence number
     * Primitive - safe to return
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Get issue date
     * LocalDate is immutable (cannot be changed after creation)
     * So safe to return the same instance
     */
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    // ============================================
    // BUSINESS LOGIC - All methods return NEW objects
    // ============================================
    
    /**
     * Get formatted ID string (e.g., "P-2024-001")
     */
    public String getFormattedId() {
        return String.format("%s-%04d-%03d", prefix, year, sequenceNumber);
    }
    
    /**
     * Check if this ID is from current year
     */
    public boolean isCurrentYear() {
        return year == Year.now().getValue();
    }
    
    /**
     * Check if this ID is older than given years
     */
    public boolean isOlderThan(int years) {
        return (Year.now().getValue() - year) > years;
    }
    
    /**
     * Get age of this ID in years
     */
    public int getAgeInYears() {
        return Year.now().getValue() - year;
    }
    
    /**
     * Create a NEW ID with incremented sequence number
     * IMPORTANT: This does NOT modify this object - it creates a NEW one
     * This is how immutable objects handle "changes"
     */
    public PrisonerID getNextSequence() {
        // Create and return a NEW PrisonerID object
        return new PrisonerID(prefix, year, sequenceNumber + 1, issueDate);
    }
    
    /**
     * Create a NEW ID for next year
     * Again, returns NEW object - doesn't modify this one
     */
    public PrisonerID getNextYear() {
        return new PrisonerID(prefix, year + 1, sequenceNumber, LocalDate.now());
    }
    
    // ============================================
    // OBJECT METHODS - Required for proper behavior
    // ============================================
    
    /**
     * Two IDs are equal if all components match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PrisonerID that = (PrisonerID) obj;
        return year == that.year &&
               sequenceNumber == that.sequenceNumber &&
               prefix.equals(that.prefix) &&
               issueDate.equals(that.issueDate);
    }
    
    /**
     * Hash code based on all fields
     * Since object is immutable, hash code never changes!
     * Safe to use as HashMap key
     */
    @Override
    public int hashCode() {
        return Objects.hash(prefix, year, sequenceNumber, issueDate);
    }
    
    /**
     * String representation
     */
    @Override
    public String toString() {
        return getFormattedId();
    }
    
    /**
     * For sorting/comparison
     */
    public int compareTo(PrisonerID other) {
        // Compare by year, then sequence number
        int yearCompare = Integer.compare(this.year, other.year);
        if (yearCompare != 0) {
            return yearCompare;
        }
        return Integer.compare(this.sequenceNumber, other.sequenceNumber);
    }
    
    // ============================================
    // STATIC FACTORY METHODS
    // ============================================
    
    /**
     * Parse ID from formatted string (e.g., "P-2024-001")
     */
    public static PrisonerID parse(String formatted) {
        if (formatted == null || formatted.isBlank()) {
            throw new IllegalArgumentException("Formatted ID cannot be empty");
        }
        
        String[] parts = formatted.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid ID format: " + formatted);
        }
        
        try {
            String prefix = parts[0];
            int year = Integer.parseInt(parts[1]);
            int sequence = Integer.parseInt(parts[2]);
            
            return new PrisonerID(prefix, year, sequence);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format: " + formatted, e);
        }
    }
    
    /**
     * Generate next ID in sequence
     */
    public static PrisonerID generateNext(PrisonerID current) {
        return current.getNextSequence();
    }
    
    /**
     * Create ID for current year
     */
    public static PrisonerID forCurrentYear(int sequenceNumber) {
        return new PrisonerID("P", Year.now().getValue(), sequenceNumber);
    }
}

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ** CUSTOM IMMUTABLE TYPE (advanced requirement!)
// ** final class (cannot be extended)
// ** All fields are private final (cannot be changed)
// ** No setter methods
// ** Getters return immutable types or primitives
// ** Methods that "modify" return NEW objects
// ** Proper equals() and hashCode()
// ** Thread-safe by design
// ** Static factory methods
// ** Input validation
// ** Date API (LocalDate, Year)

// ============================================
// WHY THIS IS IMMUTABLE:
// ============================================
// 1. Class is final → Cannot be subclassed
// 2. All fields are final → Cannot be reassigned
// 3. No setters → No way to modify state
// 4. All fields are immutable types:
//    - String (immutable)
//    - int (primitive - copied by value)
//    - LocalDate (immutable)
// 5. Any "modification" creates NEW object

// ============================================
// USAGE EXAMPLES:
// ============================================
//
// // Create immutable ID
// PrisonerID id = new PrisonerID("P", 2024, 1);
// System.out.println(id.getFormattedId());  // "P-2024-001"
//
// // Try to "modify" - creates NEW object
// PrisonerID nextId = id.getNextSequence();
// System.out.println(id.getFormattedId());      // Still "P-2024-001"
// System.out.println(nextId.getFormattedId());  // "P-2024-002"
//
// // Original object never changes!
// // This is the key to immutability
//
// // Safe to use in HashMap
// Map<PrisonerID, Prisoner> map = new HashMap<>();
// map.put(id, prisoner);  // Safe - id cannot change
//
// // Thread-safe sharing
// // Multiple threads can read id simultaneously
// // No synchronization needed!