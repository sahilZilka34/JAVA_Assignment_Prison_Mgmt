package manager;


import model.Prisoner;
import nio2.ReportFileService;
import model.Cell;
import enums.SecurityLevel;
import enums.CellType;
import records.VisitRecord;
import localisation.LocaleManager;
import records.IncidentReport;
import exceptions.PrisonerNotFoundException;
import exceptions.CellCapacityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import gatherer.GathererService;

import java.util.Scanner;

import analytics.PrisonAnalyticsService;
import concurrency.CellProcessingService;

import java.util.List;

/**
 * PrisonMenu - Interactive menu system for Prison Management
 * 
 * Implements 12 user stories demonstrating all Java features
 */
public class PrisonMenu {
    
    private Prison prison;
    private Scanner scanner;
    private boolean running;
    
    public PrisonMenu() {
        this.prison = new Prison("Central State Prison");
        this.scanner = new Scanner(System.in);
        this.running = true;
        
        // Add some sample data for testing
        initializeSampleData();
    }
    
    /**
     * Initialize with sample data for demonstration
     */
    private void initializeSampleData() {
        // Add sample prisoners
        prison.addPrisoner(new Prisoner("John Doe", "P-2024-001",
            LocalDate.of(1990, 5, 15), 10, "Robbery"));
        prison.addPrisoner(new Prisoner("Jane Smith", "P-2024-002",
            LocalDate.of(1985, 8, 20), 25, "Murder"));
        prison.addPrisoner(new Prisoner("Bob Wilson", "P-2024-003",
            LocalDate.of(1995, 3, 10), 3, "Theft"));
        // Add more varied sample data so analytics has something to work with
        prison.addPrisoner(new Prisoner("Alice Brown",  "P-2024-004", LocalDate.of(1988, 1, 22), 15, "Fraud"));
        prison.addPrisoner(new Prisoner("Carlos Diaz",  "P-2024-005", LocalDate.of(1992, 7, 4),  8,  "Assault"));
        prison.addPrisoner(new Prisoner("Emma White",   "P-2024-006", LocalDate.of(1979, 11, 30), 20, "Murder"));
        // Add sample cells
        prison.addCell(new Cell("A-101", CellType.STANDARD, SecurityLevel.HIGH));
        prison.addCell(new Cell("A-102", CellType.STANDARD, SecurityLevel.HIGH));
        prison.addCell(new Cell("B-201", CellType.SHARED, SecurityLevel.LOW));
        prison.addCell(new Cell("C-301", CellType.SOLITARY, SecurityLevel.MAXIMUM));

        prison.addPrisoner(new Prisoner("Bob Wilson", "P-2024-003",
        LocalDate.of(1995, 3, 10), 3, "Theft"));
        prison.addPrisoner(new Prisoner("Alice Brown", "P-2024-004",
        LocalDate.of(1988, 1, 22), 15, "Fraud"));
        prison.addPrisoner(new Prisoner("Emma White",  "P-2024-006",
        LocalDate.of(1979, 11, 30), 20, "Murder"));
    }
    
    /**
     * Main menu loop
     */
    public void start() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   PRISON MANAGEMENT SYSTEM");
        System.out.println("   " + prison.getPrisonName());
        System.out.println("=".repeat(60));
        
        while (running) {
            displayMenu();
            int choice = getIntInput("\nEnter your choice: ");
            processChoice(choice);
        }
        
        scanner.close();
        System.out.println("\nThank you for using Prison Management System!");
    }
    
    /**
     * Display main menu
     */
    private void displayMenu() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("MAIN MENU");
        System.out.println("-".repeat(60));
        System.out.println("1.  Admit New Prisoner");
        System.out.println("2.  View All Prisoners");
        System.out.println("3.  Search Prisoner by ID");
        System.out.println("4.  Filter by Security Level (Lambdas)");
        System.out.println("5.  Filter by Sentence Length (Method References)");
        System.out.println("6.  Add New Cell");
        System.out.println("7.  Assign Prisoner to Cell");
        System.out.println("8.  View Cell Occupancy Report");
        System.out.println("9.  Record Visitor Visit (Records)");
        System.out.println("10. Report Security Incident (Records)");
        System.out.println("11. View Sorted Prisoner List (Method References)");
        System.out.println("12. Generate Security Report (All Features)");
        System.out.println("13. Run Prison Analytics (Streams Demo)");
        System.out.println("14. Demonstrate Pattern Matching Switch (Sealed Events)");
        System.out.println("15. Concurrency Demo (ExecutorService + Callable)");
        System.out.println("16. NIO2 File I/O Demo");
        System.out.println("17. Localisation Demo (English / Irish)");
        System.out.println("18. Stream Gatherers Demo (Java 25 Bonus)");
        System.out.println("0.  Exit");
        System.out.println("-".repeat(60));
    }
    
    /**
     * Process user menu choice
     */
    private void processChoice(int choice) {
        System.out.println();
        
        switch (choice) {
            case 1 -> admitNewPrisoner();
            case 2 -> viewAllPrisoners();
            case 3 -> searchPrisonerById();
            case 4 -> filterBySecurityLevel();
            case 5 -> filterBySentenceLength();
            case 6 -> addNewCell();
            case 7 -> assignPrisonerToCell();
            case 8 -> viewCellOccupancy();
            case 9 -> recordVisit();
            case 10 -> reportIncident();
            case 11 -> viewSortedPrisoners();
            case 12 -> generateSecurityReport();
            case 13 -> runAnalytics();
            case 14 -> demonstratePatternMatching();
            case 15 -> runConcurrencyDemo();
            case 16 -> runNio2Demo();
            case 17 -> runLocalisationDemo();
            case 0 -> exitSystem();
            case 18 -> runGathererDemo();
            default -> System.out.println("❌ Invalid choice! Please try again.");
        }
        
        if (choice != 0) {
            waitForEnter();
        }
    }
    
    // ============================================
    // USER STORY 1: Admit New Prisoner
    // ============================================
    private void admitNewPrisoner() {
        printHeader("USER STORY 1: ADMIT NEW PRISONER");
        
        String name = getStringInput("Enter prisoner name: ");
        String id = getStringInput("Enter prisoner ID (format P-YYYY-XXX): ");
        
        System.out.println("Enter date of birth:");
        int year = getIntInput("  Year (e.g., 1990): ");
        int month = getIntInput("  Month (1-12): ");
        int day = getIntInput("  Day (1-31): ");
        LocalDate dob = LocalDate.of(year, month, day);
        
        int sentence = getIntInput("Enter sentence (years): ");
        String crime = getStringInput("Enter crime type: ");
        
        try {
            Prisoner prisoner = new Prisoner(name, id, dob, sentence, crime);
            prison.addPrisoner(prisoner);
            
            System.out.println("\n** SUCCESS!");
            System.out.println("Prisoner admitted: " + prisoner.getName());
            System.out.println("Assigned security level: " + prisoner.getSecurityLevel());
            System.out.println("\n*Java Features Demonstrated:");
            System.out.println("  - Constructors with validation");
            System.out.println("  - Inheritance (Prisoner extends Person)");
            System.out.println("  - Enums (auto-assigned SecurityLevel)");
            System.out.println("  - Date API (LocalDate)");
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
    
    // ============================================
    // USER STORY 2: View All Prisoners
    // ============================================
    private void viewAllPrisoners() {
        printHeader("USER STORY 2: VIEW ALL PRISONERS");
        
        List<Prisoner> prisoners = prison.getAllPrisoners();
        
        if (prisoners.isEmpty()) {
            System.out.println("No prisoners in the system.");
            return;
        }
        
        System.out.println("Total prisoners: " + prisoners.size());
        System.out.println("\n" + "-".repeat(80));
        System.out.printf("%-15s %-25s %-10s %-15s%n", "ID", "Name", "Sentence", "Security");
        System.out.println("-".repeat(80));
        
        for (Prisoner p : prisoners) {
            System.out.printf("%-15s %-25s %-10d %-15s%n",
                p.getId(),
                p.getName(),
                p.getSentenceYears(),
                p.getSecurityLevel().name()
            );
        }
        
        System.out.println("\n*Java Features Demonstrated:");
        System.out.println("  - ArrayList iteration");
        System.out.println("  - Defensive copying (getAll returns copy)");
        System.out.println("  - Encapsulation (getters)");
    }
    
    // ============================================
    // USER STORY 3: Search by ID (Exception Handling)
    // ============================================
    private void searchPrisonerById() {
        printHeader("USER STORY 3: SEARCH PRISONER BY ID");
        
        String id = getStringInput("Enter prisoner ID: ");
        
        try {
            // Simulate search that might throw exception
            Prisoner found = findPrisonerOrThrow(id);
            
            System.out.println("\n** PRISONER FOUND:");
            System.out.println(found.getFullDetails());
            System.out.println("Release date: " + found.calculateReleaseDate());
            
        } catch (PrisonerNotFoundException e) {
            System.out.println("\n❌ CHECKED EXCEPTION CAUGHT:");
            System.out.println("   " + e.getMessage());
            System.out.println("   Prisoner ID: " + e.prisonerId());
        }
        
        System.out.println("\n*Java Features Demonstrated:");
        System.out.println("  - CHECKED EXCEPTION (PrisonerNotFoundException)");
        System.out.println("  - try-catch blocks");
        System.out.println("  - Exception handling");
    }
    
    private Prisoner findPrisonerOrThrow(String id) throws PrisonerNotFoundException {
        for (Prisoner p : prison.getAllPrisoners()) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        throw new PrisonerNotFoundException("Prisoner not found", id);
    }
    
    // ============================================
    // USER STORY 4: Filter by Security Level (LAMBDAS)
    // ============================================
    private void filterBySecurityLevel() {
        printHeader("USER STORY 4: FILTER BY SECURITY LEVEL");
        
        System.out.println("Select security level:");
        System.out.println("1. LOW");
        System.out.println("2. MEDIUM");
        System.out.println("3. HIGH");
        System.out.println("4. MAXIMUM");
        
        int choice = getIntInput("\nEnter choice: ");
        SecurityLevel level = switch (choice) {
            case 1 -> SecurityLevel.LOW;
            case 2 -> SecurityLevel.MEDIUM;
            case 3 -> SecurityLevel.HIGH;
            case 4 -> SecurityLevel.MAXIMUM;
            default -> SecurityLevel.MEDIUM;
        };
        
        // LAMBDA EXPRESSION!
        List<Prisoner> filtered = prison.findBySecurityLevel(level);
        
        System.out.println("\n" + level.name() + " security prisoners: " + filtered.size());
        filtered.forEach(p -> System.out.println("  - " + p.getName() + " (" + p.getId() + ")"));
        
        System.out.println("\n  Java Features Demonstrated:");
        System.out.println("  - LAMBDAS (p -> p.getSecurityLevel() == level)");
        System.out.println("  - Predicate functional interface");
        System.out.println("  - Filter operation");
        System.out.println("  - Switch expressions");
    }
    
    // ============================================
    // USER STORY 5: Filter by Sentence (METHOD REFERENCES)
    // ============================================
    private void filterBySentenceLength() {
        printHeader("USER STORY 5: FILTER BY SENTENCE LENGTH");
        
        int years = getIntInput("Show prisoners with sentence longer than (years): ");
        
        // Lambda filtering
        List<Prisoner> filtered = prison.findBySentenceLongerThan(years);
        
        // METHOD REFERENCE sorting!
        List<Prisoner> sorted = prison.getSortedBySentence();
        
        System.out.println("\nFound " + filtered.size() + " prisoners:");
        for (Prisoner p : filtered) {
            System.out.printf("  - %-20s : %d years%n", p.getName(), p.getSentenceYears());
        }
        
        System.out.println("\n*Java Features Demonstrated:");
        System.out.println("  - LAMBDAS with custom condition");
        System.out.println("  - METHOD REFERENCES (Prisoner::getSentenceYears)");
        System.out.println("  - Comparator with method references");
    }
    
    // ============================================
    // USER STORY 6: Add New Cell
    // ============================================
    private void addNewCell() {
        printHeader("USER STORY 6: ADD NEW CELL");
        
        String cellNumber = getStringInput("Enter cell number (e.g., A-101): ");
        
        System.out.println("\nSelect cell type:");
        System.out.println("1. STANDARD");
        System.out.println("2. SOLITARY");
        System.out.println("3. MEDICAL");
        System.out.println("4. SHARED");
        int typeChoice = getIntInput("Choice: ");
        
        CellType type = switch (typeChoice) {
            case 1 -> CellType.STANDARD;
            case 2 -> CellType.SOLITARY;
            case 3 -> CellType.MEDICAL;
            case 4 -> CellType.SHARED;
            default -> CellType.STANDARD;
        };
        
        System.out.println("\nSelect security level:");
        System.out.println("1. LOW  2. MEDIUM  3. HIGH  4. MAXIMUM");
        int levelChoice = getIntInput("Choice: ");
        
        SecurityLevel level = switch (levelChoice) {
            case 1 -> SecurityLevel.LOW;
            case 2 -> SecurityLevel.MEDIUM;
            case 3 -> SecurityLevel.HIGH;
            case 4 -> SecurityLevel.MAXIMUM;
            default -> SecurityLevel.MEDIUM;
        };
        
        Cell cell = new Cell(cellNumber, type, level);
        prison.addCell(cell);
        
        System.out.println("\n** Cell added: " + cell);
        System.out.println("\n*Java Features Demonstrated:");
        System.out.println("  - Enums (CellType, SecurityLevel)");
        System.out.println("  - Constructor with multiple parameters");
        System.out.println("  - Validation logic");
    }
    
    // ============================================
    // USER STORY 7: Assign to Cell
    // ============================================
    private void assignPrisonerToCell() {
        printHeader("USER STORY 7: ASSIGN PRISONER TO CELL");
        
        String prisonerId = getStringInput("Enter prisoner ID: ");
        String cellNumber = getStringInput("Enter cell number: ");
        
        try {
            Prisoner prisoner = findPrisonerOrThrow(prisonerId);
            Cell cell = findCellOrThrow(cellNumber);
            
            // Check capacity (might throw unchecked exception)
            if (cell.isFull()) {
                throw new CellCapacityException(
                    "Cell is at full capacity",
                    cell.getCellNumber(),
                    cell.getCapacity(),
                    cell.getOccupantCount() + 1
                );
            }
            
            boolean assigned = cell.assignPrisoner(prisoner);
            
            if (assigned) {
                System.out.println("\n** Assignment successful!");
            }
            
        } catch (PrisonerNotFoundException e) {
            System.out.println("❌ Prisoner not found: " + e.prisonerId());
        } catch (CellCapacityException e) {
            System.out.println("\n❌ UNCHECKED EXCEPTION CAUGHT:");
            System.out.println("   " + e.getMessage());
            System.out.println("   Over capacity by: " + e.getOverCapacityBy());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        
        System.out.println("\n*Java Features Demonstrated:");
        System.out.println("  - UNCHECKED EXCEPTION (CellCapacityException)");
        System.out.println("  - Interface (Assignable)");
        System.out.println("  - Business validation");
    }
    
    private Cell findCellOrThrow(String cellNumber) throws Exception {
        for (Cell c : prison.getAllCells()) {
            if (c.getCellNumber().equalsIgnoreCase(cellNumber)) {
                return c;
            }
        }
        throw new Exception("Cell not found: " + cellNumber);
    }
    
    // ============================================
    // USER STORY 8: Cell Occupancy (DEFENSIVE COPYING)
    // ============================================
    private void viewCellOccupancy() {
        printHeader("USER STORY 8: CELL OCCUPANCY REPORT");
        
        List<Cell> cells = prison.getAllCells();
        
        if (cells.isEmpty()) {
            System.out.println("No cells in the system.");
            return;
        }
        
        System.out.println("Total cells: " + cells.size());
        System.out.println("\n" + "-".repeat(70));
        System.out.printf("%-10s %-15s %-12s %-10s %-10s%n",
            "Cell", "Type", "Security", "Occupancy", "Status");
        System.out.println("-".repeat(70));
        
        for (Cell cell : cells) {
            System.out.printf("%-10s %-15s %-12s %d/%-7d %-10s%n",
                cell.getCellNumber(),
                cell.getCellType().name(),
                cell.getSecurityLevel().name(),
                cell.getOccupantCount(),
                cell.getCapacity(),
                cell.isAvailable() ? "Available" : "Full"
            );
        }
        
        System.out.println("\n*Java Features Demonstrated:");
        System.out.println("  - DEFENSIVE COPYING (cell.getOccupants() returns copy)");
        System.out.println("  - ArrayList operations");
        System.out.println("  - Encapsulation");
    }
    
    // ============================================
    // USER STORY 9: Record Visit (RECORDS)
    // ============================================
    private void recordVisit() {
        printHeader("USER STORY 9: RECORD VISITOR VISIT");
        
        String visitorName = getStringInput("Visitor name: ");
        String prisonerId = getStringInput("Prisoner ID to visit: ");
        int duration = getIntInput("Visit duration (minutes): ");
        String purpose = getStringInput("Purpose of visit: ");
        
        // Create RECORD
        VisitRecord visit = new VisitRecord(
            visitorName,
            prisonerId,
            LocalDateTime.now(),
            duration,
            purpose,
            true  // approved
        );
        
        System.out.println("\n** Visit recorded!");
        System.out.println(visit);
        System.out.println("\nVisit details:");
        System.out.println("  Visitor: " + visit.visitorName());
        System.out.println("  End time: " + visit.calculateEndTime());
        System.out.println("  Status: " + visit.getStatus());
        
        System.out.println("\n*Java Features Demonstrated:");
        System.out.println("  - RECORDS (VisitRecord)");
        System.out.println("  - Auto-generated getters");
        System.out.println("  - Immutable data");
        System.out.println("  - Factory methods");
    }
    
    // ============================================
    // USER STORY 10: Report Incident (RECORDS + VARARGS)
    // ============================================
    private void reportIncident() {
        printHeader("USER STORY 10: REPORT SECURITY INCIDENT");
        
        String reportId = getStringInput("Report ID (e.g., INC-2024-001): ");
        String description = getStringInput("Incident description: ");
        String reporter = getStringInput("Reported by: ");
        
        System.out.print("Number of prisoners involved: ");
        int count = getIntInput("");
        
        String[] prisonerIds = new String[count];
        for (int i = 0; i < count; i++) {
            prisonerIds[i] = getStringInput("  Prisoner " + (i+1) + " ID: ");
        }
        
        // VARARGS in action!
        IncidentReport incident = IncidentReport.createMajorIncident(
            reportId,
            description,
            reporter,
            prisonerIds  // Array passed to varargs
        );
        
        System.out.println("\n** Incident reported!");
        System.out.println(incident.generateSummary());
        
        System.out.println("*Java Features Demonstrated:");
        System.out.println("  - RECORDS (IncidentReport)");
        System.out.println("  - VARARGS (multiple prisoner IDs)");
        System.out.println("  - DEFENSIVE COPYING (array in record)");
        System.out.println("  - Arrays");
    }
    
    // ============================================
    // USER STORY 11: Sorted List (METHOD REFERENCES)
    // ============================================
    private void viewSortedPrisoners() {
        printHeader("USER STORY 11: VIEW SORTED PRISONERS");
        
        System.out.println("Sort by:");
        System.out.println("1. Name");
        System.out.println("2. ID");
        System.out.println("3. Sentence length");
        
        int choice = getIntInput("Choice: ");
        
        List<Prisoner> sorted = switch (choice) {
            case 1 -> prison.getSortedByName();      // Prisoner::getName
            case 2 -> prison.getSortedById();        // Prisoner::getId
            case 3 -> prison.getSortedBySentence();  // Prisoner::getSentenceYears
            default -> prison.getSortedByName();
        };
        
        System.out.println("\nSorted prisoners:");
        for (Prisoner p : sorted) {
            System.out.printf("  %-20s %-15s %d years%n",
                p.getName(), p.getId(), p.getSentenceYears());
        }
        
        System.out.println("\n*Java Features Demonstrated:");
        System.out.println("  - METHOD REFERENCES (Prisoner::getName, etc.)");
        System.out.println("  - Comparator.comparing()");
        System.out.println("  - Sorting with method references");
    }
    
    // ============================================
    // USER STORY 12: Security Report (ALL FEATURES)
    // ============================================
    private void generateSecurityReport() {
        printHeader("USER STORY 12: COMPREHENSIVE SECURITY REPORT");
        
        prison.generateSecurityReport();
        
        System.out.println("\n*Java Features Demonstrated (COMBINED):");
        System.out.println("  - LAMBDAS (filtering by security level)");
        System.out.println("  - METHOD REFERENCES (sorting)");
        System.out.println("  - final/effectively final (variables in lambdas)");
        System.out.println("  - Stream operations");
        System.out.println("  - StringBuilder");
    }

    
    
    // ============================================
    // USER STORY 13: STREAM ANALYTICS
    // ============================================
    
    private void runAnalytics() {
        printHeader("USER STORY 13: STREAM ANALYTICS");
        PrisonAnalyticsService analytics = new PrisonAnalyticsService(prison.getAllPrisoners());
        analytics.runAllAnalytics();
        System.out.println("\nJava Features Demonstrated:");
        System.out.println("  - Stream terminal ops: count, min, max, findFirst, findAny");
        System.out.println("  - Stream terminal ops: allMatch, anyMatch, noneMatch, forEach");
        System.out.println("  - Collectors: groupingBy, partitioningBy, toMap");
        System.out.println("  - Intermediate ops: filter, map, distinct, sorted, limit");
        System.out.println("  - Consumer, Function, Supplier, Predicate (all 4 lambda types)");
    }
    

    // ============================================
    // USER STORY 14: PATTERN MATCHING SWITCH
    // ============================================

    private void demonstratePatternMatching() {
    printHeader("USER STORY 14: PATTERN MATCHING SWITCH");

    // Create one of each sealed event type
    List<events.PrisonEvent> events = List.of(
        new events.Admission(
            java.time.LocalDateTime.now(),
            "P-2024-001", "John Doe", "A-101", "New intake"),
        new events.Release(
            java.time.LocalDateTime.now(),
            "P-2024-003", "Bob Wilson", "SENTENCE_COMPLETE", "Home"),
        new events.Transfer(
            java.time.LocalDateTime.now(),
            "P-2024-002", "Jane Smith", "A-101", "C-301", "Security upgrade")
    );

    System.out.println("Processing " + events.size() + " prison events:\n");

    for (events.PrisonEvent event : events) {

        // PATTERN MATCHING SWITCH — the key OOP2 feature
        // Each case binds the event to a typed variable (a, r, t)
        // giving you direct access to that record's specific fields
        String description = switch (event) {
            case events.Admission a ->
                "ADMISSION  | Prisoner: " + a.prisonerName()
                + " -> Cell: " + a.cellNumber()
                + " | Reason: " + a.reason();
            case events.Release r ->
                "RELEASE    | Prisoner: " + r.prisonerName()
                + " | Type: " + r.releaseType()
                + " | Destination: " + r.destination();
            case events.Transfer t ->
                "TRANSFER   | Prisoner: " + t.prisonerName()
                + " | " + t.fromCell() + " -> " + t.toCell()
                + " | Reason: " + t.reason();
        };

        System.out.println(description);
        System.out.println("  Timestamp : " + event.timestamp());
        System.out.println("  Prisoner ID: " + event.prisonerId());
        System.out.println("  Recent?    : " + event.isRecent());
        System.out.println();
    }

        System.out.println("Java Features Demonstrated:");
        System.out.println("  - Sealed interface (PrisonEvent permits 3 types)");
        System.out.println("  - Pattern matching switch (case Admission a ->)");
        System.out.println("  - Switch is exhaustive — no default needed");
        System.out.println("  - Each case variable gives typed field access");
        System.out.println("  - Records as sealed implementations");
    }

    // ============================================
    // USER STORY 15: CONCURRENCY
    // ============================================
    private void runConcurrencyDemo() {
        printHeader("USER STORY 15: CONCURRENCY - ExecutorService + Callable");
        try {
            CellProcessingService service = new CellProcessingService(
                prison.getAllCells(),
                prison.getAllPrisoners()
            );
            service.runAll();
        } catch (InterruptedException e) {
            System.out.println("Concurrency demo interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        System.out.println("\nJava Features Demonstrated:");
        System.out.println("  - ExecutorService (thread pool management)");
        System.out.println("  - Callable<T> (task that returns a value)");
        System.out.println("  - Future<T> (receipt for an async result)");
        System.out.println("  - invokeAll() (submit all tasks at once)");
        System.out.println("  - executor.shutdown() (clean thread pool teardown)");
        System.out.println("  - Stream + lambda to build task list");
    }
    
    // ============================================
    // USER STORY 16: NIO2
    // ============================================
    private void runNio2Demo() {
        printHeader("USER STORY 16: NIO2 FILE I/O");
        try {
            ReportFileService fileService = new ReportFileService();
            fileService.runAll(prison.getAllPrisoners());
        } catch (java.io.IOException e) {
            System.out.println("File operation failed: " + e.getMessage());
        }
        System.out.println("\nJava Features Demonstrated:");
        System.out.println("  - Path and Paths.get() (file addressing)");
        System.out.println("  - Files.createDirectories() (create folders)");
        System.out.println("  - Files.writeString() with CREATE + APPEND");
        System.out.println("  - Files.writeString() with TRUNCATE_EXISTING");
        System.out.println("  - Files.readString() (read entire file)");
        System.out.println("  - Files.exists(), size(), getLastModifiedTime()");
    }
        
    // ============================================
// USER STORY 17: LOCALISATION
    // ============================================
    private void runLocalisationDemo() {
        printHeader("USER STORY 17: LOCALISATION - ResourceBundle");
        LocaleManager localeManager = new LocaleManager();
        localeManager.runDemo();
        System.out.println("Java Features Demonstrated:");
        System.out.println("  - ResourceBundle.getBundle() (loads .properties file)");
        System.out.println("  - Locale.ENGLISH and new Locale(\"ga\") for Irish");
        System.out.println("  - bundle.getString(key) (key-value lookup)");
        System.out.println("  - Runtime language switching without code changes");
        System.out.println("  - MissingResourceException handling (graceful fallback)");
    }
    
    // ============================================
    // USER STORY 18: STREAM GATHERERS (BONUS)
    // ============================================
    private void runGathererDemo() {
        printHeader("USER STORY 18: STREAM GATHERERS - Java 25 Bonus");
        GathererService gathererService = new GathererService(prison.getAllPrisoners());
        gathererService.runAll();
        System.out.println("\nJava Features Demonstrated:");
        System.out.println("  - Gatherers.windowFixed(n)   (fixed-size batching)");
        System.out.println("  - Gatherers.windowSliding(n) (overlapping windows)");
        System.out.println("  - Gatherers.scan()           (running accumulation)");
        System.out.println("  - stream.gather() as a custom intermediate operation");
    }
    
    // ============================================
    // HELPER METHODS
    // ============================================
    
    private void exitSystem() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   DEMONSTRATION COMPLETE");
        System.out.println("=".repeat(60));
        System.out.println("\n✨ All 12 user stories demonstrated!");
        System.out.println("✨ All 29 Java features showcased!");
        System.out.println("\nThank you for using Prison Management System.");
        running = false;
    }
    
    private void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   " + title);
        System.out.println("=".repeat(60));
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
    
    private void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}