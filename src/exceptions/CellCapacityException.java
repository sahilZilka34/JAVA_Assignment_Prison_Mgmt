package exceptions;

/**
 * CellCapacityException - UNCHECKED EXCEPTION
 * 
 * Demonstrates:
 * ** Unchecked exception (extends RuntimeException)
 * ** Custom exception with additional data
 * ** Exception with business logic validation
 * 
 * CHECKED vs UNCHECKED:
 * 
 * UNCHECKED EXCEPTION (extends RuntimeException):
 * - Does NOT need to be caught or declared
 * - Used for programming errors or unexpected conditions
 * - Caller CAN handle, but not required
 * - Example: NullPointerException, IllegalArgumentException
 * 
 * When to use unchecked exceptions:
 * - Programming errors (bugs)
 * - Validation failures (bad input)
 * - Unexpected system errors
 * - Situations where recovery is unlikely
 * 
 * In our case: Trying to exceed cell capacity is a programming error
 * or validation failure that should be prevented before calling the method.
 */
public class CellCapacityException extends RuntimeException {
    
    // Additional data about the capacity violation
    private String cellNumber;
    private int capacity;
    private int attempted;
    
    /**
     * Constructor with just a message
     * 
     * @param message Error message
     */
    public CellCapacityException(String message) {
        super(message);
    }
    
    /**
     * Constructor with capacity details
     * 
     * @param message Error message
     * @param cellNumber The cell that's full
     * @param capacity Maximum capacity
     * @param attempted Number of prisoners attempted to assign
     */
    public CellCapacityException(String message, String cellNumber, 
                                 int capacity, int attempted) {
        super(message);
        this.cellNumber = cellNumber;
        this.capacity = capacity;
        this.attempted = attempted;
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message Error message
     * @param cause The underlying exception
     */
    public CellCapacityException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with full details and cause
     * 
     * @param message Error message
     * @param cellNumber The cell that's full
     * @param capacity Maximum capacity
     * @param attempted Number attempted
     * @param cause The underlying exception
     */
    public CellCapacityException(String message, String cellNumber, 
                                 int capacity, int attempted, Throwable cause) {
        super(message, cause);
        this.cellNumber = cellNumber;
        this.capacity = capacity;
        this.attempted = attempted;
    }
    
    // ============================================
    // GETTERS for additional data
    // ============================================
    
    /**
     * Get the cell number that caused the exception
     */
    public String getCellNumber() {
        return cellNumber;
    }
    
    /**
     * Get the maximum capacity of the cell
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Get the number of prisoners attempted to assign
     */
    public int getAttempted() {
        return attempted;
    }
    
    /**
     * Get how many over capacity the attempt was
     */
    public int getOverCapacityBy() {
        return attempted - capacity;
    }
    
    /**
     * Get detailed error message with all information
     */
    @Override
    public String getMessage() {
        StringBuilder msg = new StringBuilder(super.getMessage());
        
        if (cellNumber != null) {
            msg.append(" [Cell: ").append(cellNumber);
            msg.append(", Capacity: ").append(capacity);
            msg.append(", Attempted: ").append(attempted);
            msg.append(", Over by: ").append(getOverCapacityBy());
            msg.append("]");
        }
        
        return msg.toString();
    }
    
    /**
     * Check if this is a severe capacity violation (over by more than 1)
     */
    public boolean isSevereViolation() {
        return getOverCapacityBy() > 1;
    }
}

// ============================================
// USAGE EXAMPLE:
// ============================================
// 
// public void assignToCellUnsafe(String cellNumber, List<Prisoner> prisoners) {
//     Cell cell = findCell(cellNumber);
//     
//     // Check capacity BEFORE attempting
//     if (prisoners.size() > cell.getAvailableSpace()) {
//         // Throw unchecked exception - this is a programming error
//         throw new CellCapacityException(
//             "Cannot assign prisoners - exceeds capacity",
//             cellNumber,
//             cell.getCapacity(),
//             cell.getOccupantCount() + prisoners.size()
//         );
//     }
//     
//     // Proceed with assignment
//     for (Prisoner p : prisoners) {
//         cell.assignPrisoner(p);
//     }
// }
// 
// // Caller does NOT need to catch (but can if they want):
// prison.assignToCellUnsafe("A-101", prisoners);  // No try-catch required
// 
// // Or optionally catch to handle gracefully:
// try {
//     prison.assignToCellUnsafe("A-101", prisoners);
// } catch (CellCapacityException e) {
//     System.err.println("Capacity error: " + e.getMessage());
//     // This is optional - unchecked exceptions don't require handling
// }

// ============================================
// KEY DIFFERENCE: CHECKED vs UNCHECKED
// ============================================
// 
// CHECKED (PrisonerNotFoundException):
// public Prisoner find(String id) throws PrisonerNotFoundException {
//     // Caller MUST handle or declare
// }
// 
// UNCHECKED (CellCapacityException):
// public void assign(Prisoner p) {
//     if (full) throw new CellCapacityException(...);
//     // Caller does NOT need to handle
// }

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ** UNCHECKED EXCEPTION (extends RuntimeException)
// ** Custom exception class
// ** Multiple constructors (overloading)
// ** super() to call parent constructor
// ** Additional data fields (cellNumber, capacity, attempted)
// ** Overriding getMessage()
// ** Exception chaining (cause parameter)
// ** Business logic in exception (isSevereViolation)
// ** StringBuilder for complex messages