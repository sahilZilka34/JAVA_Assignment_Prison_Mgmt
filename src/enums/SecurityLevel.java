package enums;

/**
 * SecurityLevel - Enum representing prisoner security classifications
 * 
 * Demonstrates:
 * ** Enum with constants
 * ** Enum with fields and constructor
 * ** Enum with methods
 * ** Business logic in enums
 * 
 * Real-world usage: Different security levels require different:
 * - Number of guards
 * - Cell types
 * - Privileges
 * - Monitoring frequency
 */
public enum SecurityLevel {
    // Enum constants with parameters
    // Each constant calls the constructor below
    LOW(1, "Minimum security - trustworthy inmates", true),
    MEDIUM(2, "Standard security - moderate risk", true),
    HIGH(3, "Maximum security - high risk inmates", false),
    MAXIMUM(5, "Super-max - extremely dangerous inmates", false);
    
    // Fields - each enum constant has these
    private final int requiredGuards;
    private final String description;
    private final boolean allowsVisitors;
    
    /**
     * Enum constructor - called for each constant above
     * Note: Enum constructors are ALWAYS private (can't create new enum values)
     * 
     * @param requiredGuards Minimum guards needed per prisoner
     * @param description Description of this security level
     * @param allowsVisitors Whether visitors are allowed
     */
    SecurityLevel(int requiredGuards, String description, boolean allowsVisitors) {
        this.requiredGuards = requiredGuards;
        this.description = description;
        this.allowsVisitors = allowsVisitors;
    }
    
    // ============================================
    // GETTER METHODS
    // ============================================
    
    /**
     * Get required number of guards for this security level
     */
    public int getRequiredGuards() {
        return requiredGuards;
    }
    
    /**
     * Get description of this security level
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if visitors are allowed for this security level
     */
    public boolean allowsVisitors() {
        return allowsVisitors;
    }
    
    // ============================================
    // BUSINESS LOGIC METHODS
    // ============================================
    
    /**
     * Check if this level is higher security than another level
     * 
     * Example: HIGH.isHigherThan(MEDIUM) returns true
     */
    public boolean isHigherThan(SecurityLevel other) {
        // ordinal() returns position in enum (LOW=0, MEDIUM=1, HIGH=2, MAXIMUM=3)
        return this.ordinal() > other.ordinal();
    }
    
    /**
     * Get the next higher security level
     * Returns null if already at maximum
     * 
     * Example: MEDIUM.getNextLevel() returns HIGH
     */
    public SecurityLevel getNextLevel() {
        SecurityLevel[] levels = values();  // values() returns all enum constants
        int currentIndex = this.ordinal();
        
        if (currentIndex < levels.length - 1) {
            return levels[currentIndex + 1];
        }
        return null;  // Already at maximum
    }
    
    /**
     * Get the previous (lower) security level
     * Returns null if already at minimum
     * 
     * Example: HIGH.getPreviousLevel() returns MEDIUM
     */
    public SecurityLevel getPreviousLevel() {
        int currentIndex = this.ordinal();
        
        if (currentIndex > 0) {
            return values()[currentIndex - 1];
        }
        return null;  // Already at minimum
    }
    
    /**
     * Check if this is maximum security
     */
    public boolean isMaximumSecurity() {
        return this == MAXIMUM;
    }
    
    /**
     * Check if this is minimum security
     */
    public boolean isMinimumSecurity() {
        return this == LOW;
    }
    
    /**
     * Calculate total guards needed for multiple prisoners
     * 
     * @param prisonerCount Number of prisoners
     * @return Total guards required
     */
    public int calculateTotalGuards(int prisonerCount) {
        return requiredGuards * prisonerCount;
    }
    
    /**
     * Get a display-friendly string representation
     */
    @Override
    public String toString() {
        return String.format("%s [Guards: %d, Visitors: %s] - %s",
                           name(),
                           requiredGuards,
                           allowsVisitors ? "Allowed" : "Not Allowed",
                           description);
    }
    
    // ============================================
    // STATIC UTILITY METHODS
    // ============================================
    
    /**
     * Parse security level from string (case-insensitive)
     * Returns null if invalid
     * 
     * Example: SecurityLevel.fromString("high") returns HIGH
     */
    public static SecurityLevel fromString(String level) {
        if (level == null) {
            return null;
        }
        
        try {
            return SecurityLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;  // Invalid level
        }
    }
    
    /**
     * Get security level based on crime severity (simple algorithm)
     * 
     * @param crimeType Type of crime
     * @return Recommended security level
     */
    public static SecurityLevel recommendLevel(String crimeType) {
        if (crimeType == null) {
            return MEDIUM;  // Default
        }
        
        String crime = crimeType.toLowerCase();
        
        // Simple classification algorithm
        if (crime.contains("murder") || crime.contains("terrorism")) {
            return MAXIMUM;
        } else if (crime.contains("assault") || crime.contains("robbery")) {
            return HIGH;
        } else if (crime.contains("theft") || crime.contains("burglary")) {
            return MEDIUM;
        } else {
            return LOW;  // Minor offenses
        }
    }
}

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ** Enum with constants (LOW, MEDIUM, HIGH, MAXIMUM)
// ** Enum with fields (requiredGuards, description, allowsVisitors)
// ** Enum with constructor
// ** Enum with instance methods (isHigherThan, getNextLevel, etc.)
// ** Enum with static methods (fromString, recommendLevel)
// ** ordinal() usage (getting enum position)
// ** values() usage (getting all enum values)
// ** Business logic in enums
// ** String methods (toUpperCase, toLowerCase, contains)