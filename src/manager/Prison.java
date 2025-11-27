package manager;

import model.Prisoner;
import model.Cell;
import enums.SecurityLevel;
import enums.CellType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.Comparator;

/**
 * Prison - Manager class for the prison system
 * 
 * Demonstrates:
 * ✅ LAMBDAS with Predicate (ADVANCED!)
 * ✅ METHOD REFERENCES (ADVANCED!)
 * ✅ final/effectively final (ADVANCED!)
 * ✅ CALL-BY-VALUE (ADVANCED!)
 * ✅ ArrayList operations
 * ✅ Functional programming
 */
public class Prison {
    
    private String prisonName;
    private List<Prisoner> prisoners;
    private List<Cell> cells;
    
    /**
     * Constructor
     */
    public Prison(String prisonName) {
        this.prisonName = prisonName;
        this.prisoners = new ArrayList<>();
        this.cells = new ArrayList<>();
    }
    
    // ============================================
    // BASIC OPERATIONS
    // ============================================
    
    /**
     * Add a prisoner
     */
    public void addPrisoner(Prisoner prisoner) {
        if (prisoner != null && !prisoners.contains(prisoner)) {
            prisoners.add(prisoner);
            System.out.println("✓ Added prisoner: " + prisoner.getName());
        }
    }
    
    /**
     * Add a cell
     */
    public void addCell(Cell cell) {
        if (cell != null && !cells.contains(cell)) {
            cells.add(cell);
            System.out.println("✓ Added cell: " + cell.getCellNumber());
        }
    }
    
    public List<Prisoner> getAllPrisoners() {
        return new ArrayList<>(prisoners); // Defensive copy
    }
    
    public List<Cell> getAllCells() {
        return new ArrayList<>(cells); // Defensive copy
    }
    
    // ============================================
    // LAMBDAS WITH PREDICATE (ADVANCED FEATURE #1)
    // ============================================
    
    /**
     * Filter prisoners using a Predicate lambda
     * 
     * Demonstrates:
     * - Lambda expressions
     * - Predicate functional interface
     * - Functional programming
     * 
     * Example usage:
     *   filterPrisoners(p -> p.getSentenceYears() > 10)
     *   filterPrisoners(p -> p.getSecurityLevel() == SecurityLevel.HIGH)
     */
    public List<Prisoner> filterPrisoners(Predicate<Prisoner> condition) {
        List<Prisoner> filtered = new ArrayList<>();
        
        for (Prisoner prisoner : prisoners) {
            if (condition.test(prisoner)) {
                filtered.add(prisoner);
            }
        }
        
        return filtered;
    }
    
    /**
     * Find prisoners by security level using lambda
     */
    public List<Prisoner> findBySecurityLevel(SecurityLevel level) {
        // LAMBDA EXPRESSION:
        // (p) -> p.getSecurityLevel() == level
        //  ^      ^
        //  |      |
        //  |      Lambda body (what to do)
        //  |
        //  Lambda parameter (input)
        
        return filterPrisoners(p -> p.getSecurityLevel() == level);
    }
    
    /**
     * Find prisoners with sentence longer than specified years
     */
    public List<Prisoner> findBySentenceLongerThan(int years) {
        // Lambda with comparison
        return filterPrisoners(p -> p.getSentenceYears() > years);
    }
    
    /**
     * Find prisoners eligible for early release
     */
    public List<Prisoner> findEligibleForEarlyRelease() {
        // Lambda calling method
        return filterPrisoners(p -> p.isEligibleForEarlyRelease());
    }
    
    /**
     * Complex lambda - multiple conditions
     */
    public List<Prisoner> findHighRiskLongSentence() {
        // Lambda with AND condition
        return filterPrisoners(p -> 
            p.getSecurityLevel() == SecurityLevel.HIGH && 
            p.getSentenceYears() > 15
        );
    }
    
    // ============================================
    // final AND effectively final (ADVANCED FEATURE #2)
    // ============================================
    
    /**
     * Demonstrates 'final' and 'effectively final' in lambdas
     * 
     * Rule: Lambdas can only access variables that are:
     * 1. Explicitly declared 'final'
     * 2. 'Effectively final' (never reassigned)
     */
    public List<Prisoner> demonstrateFinalInLambdas(int minAge) {
        // minAge is 'effectively final' - never reassigned
        // Can be used in lambda!
        
        final int maxAge = 60; // Explicitly final
        
        // Both minAge and maxAge can be used in lambda
        return filterPrisoners(p -> {
            int age = p.calculateAge();
            return age >= minAge && age <= maxAge;
        });
        
        // If we did this:
        // minAge = 25; //  Would break! Lambda can't capture reassigned variables
        
        // This demonstrates 'effectively final' concept
    }
    
    /**
     * Another example of effectively final
     */
    public void demonstrateEffectivelyFinal() {
        String searchCrime = "Robbery"; // Effectively final
        
        // Can use in lambda because it's never reassigned
        List<Prisoner> found = filterPrisoners(p -> 
            p.getCrimeType().equalsIgnoreCase(searchCrime)
        );
        
        System.out.println("Found " + found.size() + " prisoners for crime: " + searchCrime);
        
        // searchCrime = "Theft"; //  If we uncomment, lambda above breaks!
    }
    
    // ============================================
    // METHOD REFERENCES (ADVANCED FEATURE #3)
    // ============================================
    
    /**
     * Sort prisoners by name using METHOD REFERENCE
     * 
     * Method reference syntax: ClassName::methodName
     * 
     * Types of method references:
     * 1. Static method: ClassName::staticMethod
     * 2. Instance method of object: object::instanceMethod
     * 3. Instance method of arbitrary object: ClassName::instanceMethod
     * 4. Constructor: ClassName::new
     */
    public List<Prisoner> getSortedByName() {
        List<Prisoner> sorted = new ArrayList<>(prisoners);
        
        // METHOD REFERENCE VERSION (cleaner):
        sorted.sort(Comparator.comparing(Prisoner::getName));
        
        // EQUIVALENT LAMBDA VERSION (more verbose):
        // sorted.sort(Comparator.comparing(p -> p.getName()));
        
        // Prisoner::getName is a method reference to getName()
        // Much cleaner than lambda!
        
        return sorted;
    }
    
    /**
     * Sort prisoners by ID using method reference
     */
    public List<Prisoner> getSortedById() {
        List<Prisoner> sorted = new ArrayList<>(prisoners);
        
        // Method reference: Prisoner::getId
        sorted.sort(Comparator.comparing(Prisoner::getId));
        
        return sorted;
    }
    
    /**
     * Sort prisoners by sentence years (descending)
     */
    public List<Prisoner> getSortedBySentence() {
        List<Prisoner> sorted = new ArrayList<>(prisoners);
        
        // Method reference with reversed order
        sorted.sort(Comparator.comparing(Prisoner::getSentenceYears).reversed());
        
        return sorted;
    }
    
    /**
     * Print all prisoner names using method reference
     */
    public void printAllNames() {
        System.out.println("\nAll prisoners:");
        
        // METHOD REFERENCE for forEach:
        prisoners.forEach(p -> System.out.println("  - " + p.getName()));
        
        // Could also use method reference to a helper method:
        // prisoners.forEach(this::printPrisonerInfo);
    }
    
    /**
     * Helper method for method reference demonstration
     */
    private void printPrisonerInfo(Prisoner p) {
        System.out.println("  " + p.getName() + " (" + p.getId() + ")");
    }
    
    /**
     * Use method reference with custom method
     */
    public void printDetailedInfo() {
        System.out.println("\nDetailed prisoner information:");
        
        // METHOD REFERENCE to instance method of this class
        prisoners.forEach(this::printPrisonerInfo);
    }
    
    // ============================================
    // CALL-BY-VALUE DEMONSTRATION (ADVANCED FEATURE #4)
    // ============================================
    
    /**
     * Demonstrates call-by-value for primitives
     * 
     * Java ALWAYS passes by value:
     * - For primitives: copies the value
     * - For objects: copies the reference (not the object itself)
     */
    public void demonstrateCallByValue() {
        System.out.println("\n=== CALL-BY-VALUE DEMONSTRATION ===");
        
        // Test 1: Primitive (int)
        int count = 10;
        System.out.println("Before: count = " + count);
        modifyPrimitive(count);
        System.out.println("After: count = " + count);
        System.out.println("✓ Primitive unchanged (call-by-value)");
        
        // Test 2: Object reference
        Prisoner p = prisoners.isEmpty() ? null : prisoners.get(0);
        if (p != null) {
            System.out.println("\nBefore: prisoner name = " + p.getName());
            modifyObjectReference(p);
            System.out.println("After: prisoner name = " + p.getName());
            System.out.println("✓ Object reference passed by value");
            System.out.println("  (reference copied, but points to same object)");
        }
        
        // Test 3: Trying to reassign reference
        List<Prisoner> list = prisoners;
        System.out.println("\nBefore: list size = " + list.size());
        tryToReassignReference(list);
        System.out.println("After: list size = " + list.size());
        System.out.println("✓ Cannot reassign reference from inside method");
    }
    
    /**
     * Try to modify primitive - WON'T affect original
     * Demonstrates: Call-by-value for primitives
     */
    private void modifyPrimitive(int value) {
        value = 999; // Only changes local copy
        System.out.println("Inside method: value = " + value);
    }
    
    /**
     * Modify object through reference - WILL affect original
     * Demonstrates: Reference passed by value (but points to same object)
     */
    private void modifyObjectReference(Prisoner prisoner) {
        // We have a COPY of the reference, but it points to SAME object
        // So we CAN modify the object's state
        String originalName = prisoner.getName();
        // Note: We can't actually modify name (no setter), just demonstrating concept
        System.out.println("Inside method: can access object through reference");
    }
    
    /**
     * Try to reassign reference - WON'T affect original
     * Demonstrates: Can't change what the original reference points to
     */
    private void tryToReassignReference(List<Prisoner> list) {
        list = new ArrayList<>(); // Only changes local copy of reference
        list.add(null); // Modifying the NEW list, not original
        System.out.println("Inside method: reassigned to new list, size = " + list.size());
    }
    
    // ============================================
    // COMBINING FEATURES - Real-world examples
    // ============================================
    
    /**
     * Complex query combining lambdas, method references, and streams
     */
    public void generateSecurityReport() {
        System.out.println("\n=== SECURITY REPORT ===");
        System.out.println("Prison: " + prisonName);
        System.out.println("Total prisoners: " + prisoners.size());
        
        // Count by security level using lambdas
        long maxSecurity = prisoners.stream()
            .filter(p -> p.getSecurityLevel() == SecurityLevel.MAXIMUM)
            .count();
        
        long highSecurity = prisoners.stream()
            .filter(p -> p.getSecurityLevel() == SecurityLevel.HIGH)
            .count();
        
        System.out.println("MAXIMUM security: " + maxSecurity);
        System.out.println("HIGH security: " + highSecurity);
        
        // Sort by name using method reference
        System.out.println("\nPrisoners (alphabetically):");
        getSortedByName().forEach(p -> 
            System.out.println("  " + p.getName() + " - " + p.getSecurityLevel())
        );
    }
    
    public String getPrisonName() {
        return prisonName;
    }
    
    public int getPrisonerCount() {
        return prisoners.size();
    }
    
    public int getCellCount() {
        return cells.size();
    }
}