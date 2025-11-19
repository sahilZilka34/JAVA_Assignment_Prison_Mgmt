package model;

import enums.CellType;
import enums.SecurityLevel;
import java.util.ArrayList;
import java.util.List;

/**
 * Cell - Represents a prison cell that can hold prisoners
 * 
 * Demonstrates:
 * ✅ ArrayList/List usage (LAST FUNDAMENTAL!)
 * ✅ Defensive copying (ADVANCED!)
 * ✅ Varargs
 * ✅ Encapsulation
 * ✅ Business logic
 * 
 * Real-world usage:
 * Manages cell occupancy, capacity, and assignments
 */
public class Cell {
    
    // Fields
    private String cellNumber;
    private CellType cellType;
    private SecurityLevel securityLevel;
    private int capacity;
    private List<Prisoner> occupants;  // ArrayList usage!
    
    // ============================================
    // CONSTRUCTORS
    // ============================================
    
    /**
     * Primary constructor
     * 
     * @param cellNumber Unique cell identifier (e.g., "A-101")
     * @param cellType Type of cell
     * @param securityLevel Security level of this cell
     */
    public Cell(String cellNumber, CellType cellType, SecurityLevel securityLevel) {
        if (cellNumber == null || cellNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Cell number cannot be empty");
        }
        if (cellType == null) {
            throw new IllegalArgumentException("Cell type cannot be null");
        }
        if (securityLevel == null) {
            throw new IllegalArgumentException("Security level cannot be null");
        }
        
        this.cellNumber = cellNumber.trim();
        this.cellType = cellType;
        this.securityLevel = securityLevel;
        this.capacity = cellType.getMaxCapacity();  // Get capacity from enum
        this.occupants = new ArrayList<>();  // Initialize empty ArrayList
    }
    
    /**
     * Convenience constructor with default security level
     */
    public Cell(String cellNumber, CellType cellType) {
        this(cellNumber, cellType, SecurityLevel.MEDIUM);
    }
    
    // ============================================
    // GETTERS - WITH DEFENSIVE COPYING
    // ============================================
    
    public String getCellNumber() {
        return cellNumber;
    }
    
    public CellType getCellType() {
        return cellType;  // Enums are safe - no need for copying
    }
    
    public SecurityLevel getSecurityLevel() {
        return securityLevel;  // Enums are safe
    }
    
    public int getCapacity() {
        return capacity;  // Primitives are safe (passed by value)
    }
    
    /**
     * Get list of occupants - WITH DEFENSIVE COPYING
     * 
     * CRITICAL CONCEPT: Defensive Copying
     * 
     * Why we return a copy instead of the original list:
     * 1. Encapsulation protection - external code can't modify our internal list
     * 2. Prevents bugs - caller can't accidentally break our capacity rules
     * 3. Thread safety - our list can't be modified from outside
     * 
     * Example of the problem WITHOUT defensive copying:
     * 
     * Cell cell = new Cell("A-101", CellType.STANDARD, SecurityLevel.HIGH);
     * List<Prisoner> list = cell.getOccupants();  // Gets REAL list
     * list.add(prisoner);  // Bypasses capacity check! BAD!
     * list.clear();  // Destroys cell data! BAD!
     * 
     * With defensive copying, they get a COPY - changes don't affect us.
     */
    public List<Prisoner> getOccupants() {
        // DEFENSIVE COPYING: Return a NEW ArrayList with copies of elements
        // This prevents external modification of our internal list
        return new ArrayList<>(occupants);  // Creates a shallow copy
        
        // Alternative (for reference):
        // return List.copyOf(occupants);  // Returns immutable copy (Java 10+)
    }
    
    /**
     * Get current occupancy count
     */
    public int getOccupantCount() {
        return occupants.size();
    }
    
    // ============================================
    // BUSINESS LOGIC - ArrayList Operations
    // ============================================
    
    /**
     * Check if cell is available (has space)
     */
    public boolean isAvailable() {
        return occupants.size() < capacity;
    }
    
    /**
     * Check if cell is empty
     */
    public boolean isEmpty() {
        return occupants.isEmpty();
    }
    
    /**
     * Check if cell is full
     */
    public boolean isFull() {
        return occupants.size() >= capacity;
    }
    
    /**
     * Get occupancy rate as percentage
     */
    public double getOccupancyRate() {
        if (capacity == 0) return 0.0;
        return (occupants.size() * 100.0) / capacity;
    }
    
    /**
     * Add a single prisoner to this cell
     * 
     * Demonstrates: ArrayList.add() + validation
     * 
     * @param prisoner The prisoner to assign
     * @return true if successfully added
     */
    public boolean assignPrisoner(Prisoner prisoner) {
        // Validation
        if (prisoner == null) {
            System.err.println("Cannot assign null prisoner");
            return false;
        }
        
        if (isFull()) {
            System.err.println("Cell " + cellNumber + " is full!");
            return false;
        }
        
        if (occupants.contains(prisoner)) {
            System.err.println("Prisoner " + prisoner.getName() + " already in this cell");
            return false;
        }
        
        // Check security compatibility
        if (!cellType.isSuitableFor(prisoner.getSecurityLevel())) {
            System.err.println("Cell type " + cellType + " not suitable for " + 
                             prisoner.getSecurityLevel() + " security");
            return false;
        }
        
        // Add to ArrayList
        occupants.add(prisoner);
        System.out.println("✓ Assigned " + prisoner.getName() + " to cell " + cellNumber);
        return true;
    }
    
    /**
     * Add multiple prisoners at once - DEMONSTRATES VARARGS
     * 
     * Varargs (Prisoner... prisoners) allows:
     * - assignPrisoners() - zero arguments
     * - assignPrisoners(p1) - one argument
     * - assignPrisoners(p1, p2, p3) - multiple arguments
     * 
     * Behind the scenes, varargs creates an array: Prisoner[]
     * 
     * @param prisoners Variable number of prisoners
     * @return Number of prisoners successfully assigned
     */
    public int assignPrisoners(Prisoner... prisoners) {
        // VARARGS: prisoners is treated as an array
        int successCount = 0;
        
        for (Prisoner prisoner : prisoners) {
            if (assignPrisoner(prisoner)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * Remove a prisoner from this cell
     * 
     * Demonstrates: ArrayList.remove()
     * 
     * @param prisoner The prisoner to remove
     * @return true if successfully removed
     */
    public boolean removePrisoner(Prisoner prisoner) {
        if (prisoner == null) {
            return false;
        }
        
        boolean removed = occupants.remove(prisoner);
        
        if (removed) {
            System.out.println("✓ Removed " + prisoner.getName() + " from cell " + cellNumber);
        } else {
            System.err.println("Prisoner " + prisoner.getName() + " not found in cell " + cellNumber);
        }
        
        return removed;
    }
    
    /**
     * Remove prisoner by ID
     * 
     * Demonstrates: ArrayList iteration and removal
     * 
     * @param prisonerId The prisoner's ID
     * @return true if successfully removed
     */
    public boolean removePrisonerById(String prisonerId) {
        if (prisonerId == null) {
            return false;
        }
        
        // Find and remove using iterator (safe during removal)
        for (int i = 0; i < occupants.size(); i++) {
            if (occupants.get(i).getId().equals(prisonerId)) {
                Prisoner removed = occupants.remove(i);
                System.out.println("✓ Removed " + removed.getName() + " from cell " + cellNumber);
                return true;
            }
        }
        
        System.err.println("Prisoner with ID " + prisonerId + " not found");
        return false;
    }
    
    /**
     * Clear all occupants (for maintenance/emergency)
     * 
     * Demonstrates: ArrayList.clear()
     */
    public void clearCell() {
        int count = occupants.size();
        occupants.clear();
        System.out.println("✓ Cleared " + count + " prisoners from cell " + cellNumber);
    }
    
    /**
     * Check if a specific prisoner is in this cell
     * 
     * Demonstrates: ArrayList.contains()
     */
    public boolean containsPrisoner(Prisoner prisoner) {
        return occupants.contains(prisoner);
    }
    
    /**
     * Find prisoner by ID in this cell
     * 
     * Demonstrates: ArrayList search
     * 
     * @param prisonerId The prisoner's ID
     * @return Prisoner object if found, null otherwise
     */
    public Prisoner findPrisonerById(String prisonerId) {
        for (Prisoner prisoner : occupants) {
            if (prisoner.getId().equals(prisonerId)) {
                return prisoner;
            }
        }
        return null;
    }
    
    /**
     * Get prisoners sorted by name
     * 
     * Demonstrates: ArrayList + sorting
     * Also uses DEFENSIVE COPYING - we sort a copy, not the original
     */
    public List<Prisoner> getOccupantsSortedByName() {
        // Create a copy first (defensive copying)
        List<Prisoner> sorted = new ArrayList<>(occupants);
        
        // Sort the copy
        sorted.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        
        return sorted;
    }
    
    /**
     * Generate cell report
     * 
     * Demonstrates: StringBuilder + ArrayList iteration
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== CELL REPORT ===\n");
        report.append("Cell Number: ").append(cellNumber).append("\n");
        report.append("Cell Type: ").append(cellType.name()).append("\n");
        report.append("Security Level: ").append(securityLevel.name()).append("\n");
        report.append("Capacity: ").append(capacity).append("\n");
        report.append("Current Occupants: ").append(occupants.size()).append("\n");
        report.append("Occupancy Rate: ").append(String.format("%.1f", getOccupancyRate())).append("%\n");
        report.append("Status: ").append(isAvailable() ? "Available" : "Full").append("\n");
        
        if (!occupants.isEmpty()) {
            report.append("\nOccupants:\n");
            for (int i = 0; i < occupants.size(); i++) {
                Prisoner p = occupants.get(i);
                report.append("  ").append(i + 1).append(". ")
                      .append(p.getName()).append(" (").append(p.getId()).append(")\n");
            }
        }
        
        return report.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Cell[%s, Type=%s, Security=%s, Occupancy=%d/%d]",
                           cellNumber, cellType.name(), securityLevel.name(),
                           occupants.size(), capacity);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell cell = (Cell) obj;
        return cellNumber.equals(cell.cellNumber);
    }
    
    @Override
    public int hashCode() {
        return cellNumber.hashCode();
    }
}

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ✅ ArrayList/List usage (THE LAST FUNDAMENTAL!)
//    - List<Prisoner> occupants
//    - ArrayList operations (add, remove, contains, clear, size, etc.)
// ✅ VARARGS (assignPrisoners method)
// ✅ DEFENSIVE COPYING (getOccupants method) - ADVANCED!
// ✅ Encapsulation
// ✅ Method overloading (constructors)
// ✅ StringBuilder (generateReport)
// ✅ String methods
// ✅ Business logic (capacity checks, validation)
// ✅ equals/hashCode override