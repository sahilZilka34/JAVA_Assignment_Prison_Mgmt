package model;

import interfaces.Assignable;
import enums.SecurityLevel;
import enums.CellType;
import java.time.LocalDate;

/**
 * Prisoner - Represents an inmate (Complete Version)
 * 
 * Demonstrates:
 * ** Inheritance (extends Person)
 * ** Interface implementation (implements Assignable)
 * ** super() - calling parent constructor
 * ** super. - accessing parent methods/fields
 * ** Method overriding (toString, isValidId)
 * ** Polymorphism (can be treated as Person OR Assignable)
 */
public class Prisoner extends Person implements Assignable {
    
    // Prisoner-specific fields
    private int sentenceYears;
    private String crimeType;
    private LocalDate admissionDate;
    private SecurityLevel securityLevel;
    
    // ============================================
    // CONSTRUCTORS
    // ============================================
    
    /**
 * Full constructor (5 parameters)
 *
 * Demonstrates JEP 513 — Flexible Constructor Bodies (Java 25 Preview)
 *
 * BEFORE Java 25: super() MUST be the first line — no exceptions.
 * Any logic before super() was a compile error.
 *
 * AFTER Java 25 (JEP 513): Logic CAN run before super().
 * You can validate, transform, or compute values first,
 * then pass the cleaned result to the parent constructor.
 *
 * Real-world benefit: normalise/validate data before passing
 * it up the chain, rather than doing it awkwardly after.
 */
public Prisoner(String name, String id, LocalDate dateOfBirth,
               int sentenceYears, String crimeType) {

    // ============================================
    // JEP 513 — Code BEFORE super() (Java 25 only)
    // ============================================
    // Pre-process crimeType before passing to parent
    // In Java 21 this would be a compile error
    String normalisedCrime = (crimeType == null || crimeType.trim().isEmpty())
        ? "Unknown"
        : crimeType.trim();

        // Validate sentence before calling super
        if (sentenceYears <= 0) {
            throw new IllegalArgumentException("Sentence must be positive");
        }

        // NOW call super() — with clean, validated data
        // Before JEP 513 this had to be line 1 with raw, unprocessed values
        super(name, id, dateOfBirth);

        // Post-super assignments (same as before)
        this.sentenceYears = sentenceYears;
        this.crimeType = normalisedCrime;
        this.admissionDate = LocalDate.now();
        this.securityLevel = SecurityLevel.recommendLevel(normalisedCrime);
    }   
    
    /**
     * Convenience constructor (4 parameters)
     */
    public Prisoner(String name, String id, LocalDate dateOfBirth, 
                   int sentenceYears) {
        super(name, id, dateOfBirth);
        
        this.sentenceYears = sentenceYears;
        this.crimeType = "Not specified";
        this.admissionDate = LocalDate.now();
        this.securityLevel = SecurityLevel.MEDIUM;
    }
    
    /**
     * Minimal constructor (3 parameters)
     */
    public Prisoner(String name, String id, int sentenceYears) {
        this(name, id, LocalDate.now().minusYears(25), sentenceYears);
    }
    
    // ============================================
    // GETTERS
    // ============================================
    
    public int getSentenceYears() {
        return sentenceYears;
    }
    
    public String getCrimeType() {
        return crimeType;
    }
    
    public LocalDate getAdmissionDate() {
        return admissionDate;
    }
    
    // ============================================
    // BUSINESS LOGIC
    // ============================================
    
    /**
     * Calculate release date
     */
    public LocalDate calculateReleaseDate() {
        return admissionDate.plusYears(sentenceYears);
    }
    
    /**
     * Check if prisoner is eligible for early release
     */
    public boolean isEligibleForEarlyRelease() {
        long monthsServed = java.time.Period.between(
            admissionDate, 
            LocalDate.now()
        ).toTotalMonths();
        
        long totalMonths = sentenceYears * 12L;
        long requiredMonths = (long) (totalMonths * 0.75);
        
        return monthsServed >= requiredMonths && super.isAdult();
    }
    
    /**
     * Get full details using super.
     */
    public String getFullDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Prisoner Details:\n");
        details.append("  Name: ").append(super.getName()).append("\n");
        details.append("  ID: ").append(super.getId()).append("\n");
        details.append("  Age: ").append(super.calculateAge()).append("\n");
        details.append("  Sentence: ").append(this.sentenceYears).append(" years\n");
        details.append("  Crime: ").append(this.crimeType).append("\n");
        
        return details.toString();
    }
    
    /**
     * Display basic info using parent's method
     */
    public void displayBasicInfo() {
        System.out.println("Basic Info: " + super.toString());
        System.out.println("Full Info: " + this.toString());
    }
    
    /**
     * Implementation of abstract method from Person
     */
    @Override
    public boolean isValidId() {
        String id = super.getId();
        return id.matches("P-\\d{4}-\\d{3}");
    }
    
    /**
     * Override parent's toString()
     */
    @Override
    public String toString() {
        return super.toString().replace("Person", "Prisoner") 
               + String.format(" [sentence=%d years, crime=%s]", sentenceYears, crimeType);
    }
    
    // ============================================
    // ASSIGNABLE INTERFACE IMPLEMENTATION
    // ============================================
    
    /**
     * Implementation of Assignable.getSecurityLevel()
     * Required by Assignable interface
     */
    @Override
    public SecurityLevel getSecurityLevel() {
        return securityLevel;
    }
    
    /**
     * Implementation of Assignable.canBeAssignedTo()
     * Required by Assignable interface
     */
    @Override
    public boolean canBeAssignedTo(CellType cellType) {
        return cellType.isSuitableFor(this.securityLevel);
    }
    
    // Note: getId() and getName() are inherited from Person class
    // So Assignable interface requirements are satisfied!
}