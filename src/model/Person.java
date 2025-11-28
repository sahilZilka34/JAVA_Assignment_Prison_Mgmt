package model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Person - Abstract base class 
 * 
 * Demonstrates:
 * ** Abstract class
 * ** Encapsulation (private fields)
 * ** this() - constructor chaining
 * ** this. - instance variable reference
 * ** Method overloading (multiple constructors + methods)
 * ** Date API (LocalDate, Period)
 */

public abstract class Person{
    //Encapsulation - Private fields
    private String name;
    private String id;
    private LocalDate dateOfBirth;

     // ============================================
    // CONSTRUCTORS - Demonstrating this() vs this.
    // ============================================

    /**
     * PRIMARY CONSTRUCTOR (3 parameters)
     * This is where actual initialization happens.
     * 
     * Demonstrates: this. (instance variable reference)
     */

     public Person( String name, String id, LocalDate dateOfBirth){
        // DEMONSTRATION: this. = refers to instance variable
        // Without this., Java can't distinguish parameter from field
        
        // Input validation (defensive programming)

        if(name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Name cannot be empty or null");
        } if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }

        this.name = name.trim();
        this.id = id.trim().toUpperCase();
        this.dateOfBirth = dateOfBirth;
     }

     /**
     * CONVENIENCE CONSTRUCTOR (2 parameters)
     * Demonstrates: this() - calling another constructor in THIS class
     * 
     * This is METHOD OVERLOADING - same method name (Person), different parameters
     */
    public Person(String name, String id) {
        // DEMONSTRATION: this() = calls the other constructor (above)
        // MUST be the FIRST statement in constructor
        // Provides a default date of birth (30 years ago)
        this(name, id, LocalDate.now().minusYears(30));
        
        // Why use this()?
        // - Avoids code duplication (DRY principle)
        // - All validation happens in one place
        // - If we add more fields later, only update the primary constructor
        
        System.out.println("DEBUG: Created person with default DOB");
    }

     
    /**
     * 
     * this() and METHOD OVERLOADING
     * 
     * Three constructors = Method Overloading in action!
     */
    public Person(String name) {
        // Chain to the 2-parameter constructor, providing default ID
        this(name, "TEMP-" + System.currentTimeMillis());
        
        System.out.println("DEBUG: Created person with temporary ID");
    }

    // ============================================
    // GETTERS - Encapsulation
    // ============================================

    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

      /**
     * Calculate age at current date
     * Demonstrates: Date API usage
     */
    public int calculateAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    
    /**
     * Calculate age at a specific date
     * DEMONSTRATION: METHOD OVERLOADING
     * Same method name (calculateAge), different parameters
     */
    public int calculateAge(LocalDate atDate) {
        if (atDate == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (atDate.isBefore(dateOfBirth)) {
            throw new IllegalArgumentException("Date cannot be before birth date");
        }
        return Period.between(dateOfBirth, atDate).getYears();
    }
    
    /**
     * Get age in months instead of years
     * DEMONSTRATION: METHOD OVERLOADING
     * Same method name, different return type + logic
     */
    public long calculateAgeInMonths() {
        return Period.between(dateOfBirth, LocalDate.now()).toTotalMonths();
    }
    
    // ============================================
    // BUSINESS LOGIC
    // ============================================
    
    /**
     * Check if person is an adult (18+)
     */
    public boolean isAdult() {
        return calculateAge() >= 18;
    }
    
    /**
     * Abstract method - subclasses MUST implement
     * Demonstrates: Abstraction + Polymorphism
     */
    public abstract boolean isValidId();
    
    // ============================================
    // UTILITY METHODS
    // ============================================
    
    /**
     * String representation - for debugging
     */
    @Override
    public String toString() {
        return String.format("Person[name=%s, id=%s, age=%d]", 
                           name, id, calculateAge());
    }
    
    /**
     * Equality based on ID (unique identifier)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id.equals(person.id);
    }
    
    /**
     * Hash code based on ID
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

// ============================================
// ASSIGNMENT FEATURES DEMONSTRATED:
// ============================================
// ** Classes
// ** this() - constructor chaining (line 62, 77)
// ** this. - instance variable reference (line 54-56)
// ** Method overloading (3 constructors + 3 calculateAge methods)
// ** Encapsulation (private fields + public methods)
// ** Abstract class (cannot instantiate directly)
// ** Abstract methods (isValidId)
// ** Date API (LocalDate, Period)
// ** String usage (trim, toUpperCase, format)
// ** Exception handling (IllegalArgumentException)

