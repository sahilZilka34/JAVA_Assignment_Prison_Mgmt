package exceptions;

/**
 * PrisonerNotFoundException - CHECKED EXCEPTION
 * 
 * Demonstrates:
 * ** Checked exception (extends Exception)
 * ** Custom exception with constructors
 * ** Exception with additional data
 * 
 * CHECKED vs UNCHECKED:
 * 
 * CHECKED EXCEPTION (extends Exception):
 * - MUST be caught or declared in method signature
 * - Used for recoverable errors that caller should handle
 * - Compiler enforces handling
 * - Example: FileNotFoundException, IOException
 * 
 * When to use checked exceptions:
 * - Expected errors that caller can recover from
 * - Situations where caller should take action
 * - Business logic errors that are normal conditions
 * 
 * In our case: Searching for a prisoner that doesn't exist is an expected
 * situation that the caller should handle (show error message, retry, etc.)
 */
public class PrisonerNotFoundException extends Exception {
    
    // Additional data - the ID that wasn't found
    private String prisonerId;
    
    /**
     * Constructor with just a message
     * 
     * @param message Error message
     */
    public PrisonerNotFoundException(String message) {
        super(message);  // Call parent Exception constructor
    }
    
    /**
     * Constructor with message and prisoner ID
     * 
     * @param message Error message
     * @param prisonerId The ID that wasn't found
     */
    public PrisonerNotFoundException(String message, String prisonerId) {
        super(message);
        this.prisonerId = prisonerId;
    }
    
    /**
     * Constructor with message and cause
     * Useful for wrapping other exceptions
     * 
     * @param message Error message
     * @param cause The underlying exception
     */
    public PrisonerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with message, prisoner ID, and cause
     * 
     * @param message Error message
     * @param prisonerId The ID that wasn't found
     * @param cause The underlying exception
     */
    public PrisonerNotFoundException(String message, String prisonerId, Throwable cause) {
        super(message, cause);
        this.prisonerId = prisonerId;
    }
    
    /**
     * Get the prisoner ID that wasn't found
     * 
     * @return The prisoner ID, or null if not set
     */
    public String prisonerId() {
        return prisonerId;
    }
    
    /**
     * Get detailed error message including prisoner ID
     */
    @Override
    public String getMessage() {
        if (prisonerId != null) {
            return super.getMessage() + " [Prisoner ID: " + prisonerId + "]";
        }
        return super.getMessage();
    }
}

// ============================================
// USAGE EXAMPLE:
// ============================================
// 
// public Prisoner findPrisoner(String id) throws PrisonerNotFoundException {
//     Prisoner prisoner = search(id);
//     if (prisoner == null) {
//         throw new PrisonerNotFoundException("Prisoner not found", id);
//     }
//     return prisoner;
// }
// 
// // Caller MUST handle:
// try {
//     Prisoner p = prison.findPrisoner("P-2024-999");
// } catch (PrisonerNotFoundException e) {
//     System.err.println("Error: " + e.getMessage());
//     // Can recover - show error to user, try different ID, etc.
// }

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ** CHECKED EXCEPTION (extends Exception)
// ** Custom exception class
// ** Multiple constructors (overloading)
// ** super() to call parent constructor
// ** Additional data field (prisonerId)
// ** Overriding getMessage()
// ** Exception chaining (cause parameter)