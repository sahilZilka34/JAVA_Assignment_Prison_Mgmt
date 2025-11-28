package enums;

/**
 * CellType - Enum representing different types of prison cells
 * 
 * Demonstrates:
 * ** Enum with fields and methods
 * ** Enum-to-enum relationship (compatible security levels)
 * ** Arrays in enums
 */
public enum CellType {
    // Enum constants with parameters
    STANDARD(2, 10.0, "Regular cell for general population"),
    SOLITARY(1, 5.0, "Isolation cell for disciplinary purposes"),
    MEDICAL(1, 12.0, "Cell with medical facilities"),
    PROTECTIVE(2, 10.0, "Cell for at-risk inmates"),
    SHARED(4, 15.0, "Large cell for low-security inmates");
    
    // Fields
    private final int maxCapacity;
    private final double sizeInSqMeters;
    private final String description;
    
    /**
     * Constructor for CellType enum
     * 
     * @param maxCapacity Maximum number of prisoners
     * @param sizeInSqMeters Cell size in square meters
     * @param description Description of cell type
     */
    CellType(int maxCapacity, double sizeInSqMeters, String description) {
        this.maxCapacity = maxCapacity;
        this.sizeInSqMeters = sizeInSqMeters;
        this.description = description;
    }
    
    // ============================================
    // GETTER METHODS
    // ============================================
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    public double getSizeInSqMeters() {
        return sizeInSqMeters;
    }
    
    public String getDescription() {
        return description;
    }
    
    // ============================================
    // BUSINESS LOGIC METHODS
    // ============================================
    
    /**
     * Check if this cell type is suitable for a security level
     * 
     * Business rules:
     * - SOLITARY: Only for HIGH and MAXIMUM
     * - SHARED: Only for LOW
     * - Others: Compatible with most levels
     */
    public boolean isSuitableFor(SecurityLevel level) {
        switch (this) {
            case SOLITARY:
                // Only for high-security inmates
                return level == SecurityLevel.HIGH || level == SecurityLevel.MAXIMUM;
                
            case SHARED:
                // Only for low-security inmates
                return level == SecurityLevel.LOW;
                
            case MEDICAL:
                // Medical cells available for any security level
                return true;
                
            case PROTECTIVE:
                // Protective custody for any level
                return true;
                
            case STANDARD:
                // Standard cells for low, medium, high (not maximum)
                return level != SecurityLevel.MAXIMUM;
                
            default:
                return false;
        }
    }
    
    /**
     * Check if cell allows multiple occupants
     */
    public boolean allowsMultipleOccupants() {
        return maxCapacity > 1;
    }
    
    /**
     * Calculate space per person if cell is at max capacity
     */
    public double getSpacePerPerson() {
        return sizeInSqMeters / maxCapacity;
    }
    
    /**
     * Check if this is a special cell type (not standard)
     */
    public boolean isSpecialType() {
        return this != STANDARD && this != SHARED;
    }
    
    @Override
    public String toString() {
        return String.format("%s [Capacity: %d, Size: %.1fm²] - %s",
                           name(),
                           maxCapacity,
                           sizeInSqMeters,
                           description);
    }
    
    // ============================================
    // STATIC UTILITY METHODS
    // ============================================
    
    /**
     * Get recommended cell type for a security level
     * 
     * @param level Security level
     * @return Recommended cell type
     */
    public static CellType recommendFor(SecurityLevel level) {
        switch (level) {
            case LOW:
                return SHARED;
            case MEDIUM:
                return STANDARD;
            case HIGH:
                return STANDARD;
            case MAXIMUM:
                return SOLITARY;
            default:
                return STANDARD;
        }
    }
    
    /**
     * Get all cell types suitable for a security level
     * Demonstrates: Arrays usage
     * 
     * @param level Security level
     * @return Array of suitable cell types
     */
    public static CellType[] getSuitableTypes(SecurityLevel level) {
        // Count how many cell types are suitable
        int count = 0;
        for (CellType type : values()) {
            if (type.isSuitableFor(level)) {
                count++;
            }
        }
        
        // Create array of suitable types
        CellType[] suitable = new CellType[count];
        int index = 0;
        for (CellType type : values()) {
            if (type.isSuitableFor(level)) {
                suitable[index++] = type;
            }
        }
        
        return suitable;
    }
}

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ** Enum with fields
// ** Enum with constructor
// ** Enum with instance methods
// ** Enum with static methods
// ** Enum-to-enum relationship (CellType with SecurityLevel)
// ** Arrays (CellType[] in getSuitableTypes)
// ** Switch statement
// ** Business logic in enums