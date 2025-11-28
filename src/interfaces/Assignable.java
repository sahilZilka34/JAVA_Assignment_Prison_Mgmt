package interfaces;

import enums.SecurityLevel;
import enums.CellType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Assignable - Interface for entities that can be assigned to cells
 * 
 * Demonstrates:
 * ** Interface (contract that classes must implement)
 * ** Abstract methods (no implementation)
 * ** DEFAULT methods (Java 8+) - has implementation, can be overridden
 * ** STATIC methods (Java 8+) - utility methods, cannot be overridden
 * ** PRIVATE methods (Java 9+) - helper methods for default/static methods
 * 
 * Real-world usage:
 * Any entity that can be assigned to a prison cell implements this interface:
 * - Prisoners
 * - Detainees  
 * - Temporary holds
 */
public interface Assignable {
    
    // ============================================
    // ABSTRACT METHODS (No implementation - must be implemented by classes)
    // ============================================
    
    /**
     * Get unique ID of the assignable entity
     * Classes MUST implement this
     */
    String getId();
    
    /**
     * Get name of the assignable entity
     * Classes MUST implement this
     */
    String getName();
    
    /**
     * Get security level required for this entity
     * Classes MUST implement this
     */
    SecurityLevel getSecurityLevel();
    
    /**
     * Check if this entity can be assigned to a specific cell type
     * Classes MUST implement this
     * 
     * @param cellType The type of cell
     * @return true if assignment is allowed
     */
    boolean canBeAssignedTo(CellType cellType);
    
    // ============================================
    // DEFAULT METHODS (Has implementation, but can be overridden)
    // Introduced in Java 8
    // ============================================
    
    /**
     * DEFAULT METHOD - Get full display name with ID
     * 
     * Classes can use this default implementation OR override it.
     * 
     * Why default methods?
     * - Add new methods to interfaces without breaking existing implementations
     * - Provide common functionality that most classes can use
     * - Classes can still override if they need custom behavior
     */
    default String getDisplayName() {
        // Uses abstract methods (getName, getId) that implementing classes must provide
        return String.format("%s [ID: %s]", getName(), getId());
    }
    
    /**
     * DEFAULT METHOD - Generate assignment summary
     * 
     * Uses PRIVATE helper method (formatTimestamp)
     * Demonstrates default method calling private method
     */
    default String generateAssignmentSummary(CellType cellType, String cellNumber) {
        StringBuilder summary = new StringBuilder();
        summary.append("=== ASSIGNMENT SUMMARY ===\n");
        summary.append("Entity: ").append(getDisplayName()).append("\n");
        summary.append("Security Level: ").append(getSecurityLevel()).append("\n");
        summary.append("Cell Type: ").append(cellType.name()).append("\n");
        summary.append("Cell Number: ").append(cellNumber).append("\n");
        summary.append("Timestamp: ").append(formatTimestamp(LocalDateTime.now())).append("\n");
        summary.append("Status: ");
        
        if (canBeAssignedTo(cellType)) {
            summary.append("✓ APPROVED");
        } else {
            summary.append("✗ DENIED - Incompatible cell type");
        }
        
        return summary.toString();
    }
    
    /**
     * DEFAULT METHOD - Validate if assignment is safe
     * 
     * Business logic in default method
     * Can be overridden by classes with special rules
     */
    default boolean isAssignmentSafe(CellType cellType, SecurityLevel cellSecurityLevel) {
        // Rule 1: Entity's security level must match or be lower than cell's level
        if (getSecurityLevel().isHigherThan(cellSecurityLevel)) {
            return false;
        }
        
        // Rule 2: Entity must be compatible with cell type
        if (!canBeAssignedTo(cellType)) {
            return false;
        }
        
        // Rule 3: Use private helper to validate cell compatibility
        return validateCellCompatibility(cellType, getSecurityLevel());
    }
    
    /**
     * DEFAULT METHOD - Get priority level for assignment queue
     * 
     * Higher security = higher priority
     * Used when multiple entities are waiting for cells
     */
    default int getAssignmentPriority() {
        SecurityLevel level = getSecurityLevel();
        // MAXIMUM = 4, HIGH = 3, MEDIUM = 2, LOW = 1
        return level.ordinal() + 1;
    }
    
    /**
     * DEFAULT METHOD - Check if this entity requires special handling
     * 
     * Demonstrates: calling both abstract and static methods
     */
    default boolean requiresSpecialHandling() {
        SecurityLevel level = getSecurityLevel();
        // Use static method to check if level is extreme
        return isExtremeSecurityLevel(level);
    }
    
    // ============================================
    // STATIC METHODS (Utility methods - belong to interface, not instances)
    // Introduced in Java 8
    // ============================================
    
    /**
     * STATIC METHOD - Check if a security level is extreme (LOW or MAXIMUM)
     * 
     * Why static methods in interfaces?
     * - Utility methods related to the interface
     * - Don't need an instance to call
     * - Better organization (keep related utilities together)
     * 
     * Call like: Assignable.isExtremeSecurityLevel(level)
     */
    static boolean isExtremeSecurityLevel(SecurityLevel level) {
        return level == SecurityLevel.LOW || level == SecurityLevel.MAXIMUM;
    }
    
    /**
     * STATIC METHOD - Calculate total capacity needed for multiple assignables
     * 
     * Demonstrates: Varargs usage in interface!
     * 
     * @param assignables Variable number of Assignable entities
     * @return Total number of entities
     */
    static int calculateRequiredCapacity(Assignable... assignables) {
        // VARARGS - can pass 0 or more Assignable objects
        // assignables is treated as an array
        return assignables.length;
    }
    
    /**
     * STATIC METHOD - Get recommended cell type for security level
     * 
     * Uses private static helper method
     */
    static CellType getRecommendedCellType(SecurityLevel level) {
        // Validate first using private static method
        if (!isValidSecurityLevel(level)) {
            return CellType.STANDARD;  // Default fallback
        }
        
        return CellType.recommendFor(level);
    }
    
    /**
     * STATIC METHOD - Format assignment report header
     * 
     * Uses private static method for formatting
     */
    static String formatReportHeader(String reportTitle) {
        int width = 50;
        return formatCenteredText(reportTitle, width, '=');
    }
    
    /**
     * STATIC METHOD - Validate multiple assignments at once
     * 
     * Demonstrates: Varargs + static method + private method
     */
    static boolean validateAllAssignments(SecurityLevel requiredLevel, Assignable... assignables) {
        if (assignables.length == 0) {
            return false;
        }
        
        for (Assignable assignable : assignables) {
            if (!isValidForLevel(assignable, requiredLevel)) {
                return false;
            }
        }
        return true;
    }
    
    // ============================================
    // PRIVATE METHODS (Helper methods - can only be used within this interface)
    // Introduced in Java 9
    // ============================================
    
    /**
     * PRIVATE METHOD - Format timestamp for display
     * 
     * Why private methods in interfaces?
     * - Reduce code duplication in default methods
     * - Keep helper logic encapsulated
     * - Can't be accessed by implementing classes or outside
     * 
     * Used by: generateAssignmentSummary (default method)
     */
    private String formatTimestamp(LocalDateTime timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    /**
     * PRIVATE METHOD - Validate cell compatibility with security level
     * 
     * Business logic helper for default methods
     * Used by: isAssignmentSafe (default method)
     */
    private boolean validateCellCompatibility(CellType cellType, SecurityLevel securityLevel) {
        // Business rules
        switch (cellType) {
            case SOLITARY:
                // Solitary only for high security
                return securityLevel == SecurityLevel.HIGH || 
                       securityLevel == SecurityLevel.MAXIMUM;
                
            case SHARED:
                // Shared only for low security
                return securityLevel == SecurityLevel.LOW;
                
            case MEDICAL:
            case PROTECTIVE:
                // These accept any security level
                return true;
                
            case STANDARD:
                // Standard for most, but not maximum
                return securityLevel != SecurityLevel.MAXIMUM;
                
            default:
                return false;
        }
    }
    
    /**
     * PRIVATE METHOD - Additional validation helper
     * 
     * Used by: requiresSpecialHandling (default method)
     */
    private boolean needsIsolation() {
        return getSecurityLevel() == SecurityLevel.MAXIMUM;
    }
    
    // ============================================
    // PRIVATE STATIC METHODS
    // ============================================
    
    /**
     * PRIVATE STATIC METHOD - Validate security level is not null
     * 
     * Helper for static methods
     * Used by: getRecommendedCellType (static method)
     */
    private static boolean isValidSecurityLevel(SecurityLevel level) {
        return level != null;
    }
    
    /**
     * PRIVATE STATIC METHOD - Check if assignable matches required level
     * 
     * Used by: validateAllAssignments (static method)
     */
    private static boolean isValidForLevel(Assignable assignable, SecurityLevel requiredLevel) {
        if (assignable == null || requiredLevel == null) {
            return false;
        }
        return assignable.getSecurityLevel() == requiredLevel;
    }
    
    /**
     * PRIVATE STATIC METHOD - Format text centered with padding
     * 
     * Used by: formatReportHeader (static method)
     */
    private static String formatCenteredText(String text, int width, char padChar) {
        if (text.length() >= width) {
            return text;
        }
        
        int totalPadding = width - text.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        
        return String.valueOf(padChar).repeat(leftPadding) + 
               text + 
               String.valueOf(padChar).repeat(rightPadding);
    }
}

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ** Interface (contract for classes)
// ** Abstract methods (must be implemented)
// ** DEFAULT methods (can be overridden) - Java 8
//    - getDisplayName()
//    - generateAssignmentSummary()
//    - isAssignmentSafe()
//    - getAssignmentPriority()
//    - requiresSpecialHandling()
// ** STATIC methods (utility methods) - Java 8
//    - isExtremeSecurityLevel()
//    - calculateRequiredCapacity()
//    - getRecommendedCellType()
//    - formatReportHeader()
//    - validateAllAssignments()
// ** PRIVATE methods (helper methods) - Java 9
//    - formatTimestamp()
//    - validateCellCompatibility()
//    - needsIsolation()
// ** PRIVATE STATIC methods - Java 9
//    - isValidSecurityLevel()
//    - isValidForLevel()
//    - formatCenteredText()
// ** VARARGS in interface (calculateRequiredCapacity)
// ** StringBuilder usage
// ** Date API (LocalDateTime, DateTimeFormatter)
// ** String methods (format, repeat)
// ** Switch statement