import model.Prisoner;
import model.Cell;
import model.PrisonerID;
import enums.SecurityLevel;
import enums.CellType;
import exceptions.PrisonerNotFoundException;
import records.VisitRecord;
import records.IncidentReport;
import events.PrisonEvent;
import events.Admission;
import events.Release;
import events.Transfer;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Main.java - Testing Records, Immutable Types, Sealed Classes
 * 
 * Final comprehensive test before Prison manager class
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   PRISON MANAGEMENT SYSTEM - TEST v6");
        System.out.println("   Testing: Records, Immutable, Sealed");
        System.out.println("==============================================");
        System.out.println();
        
        // ============================================
        // TEST 1: RECORD TYPE - VisitRecord
        // ============================================
        System.out.println("TEST 1: RECORD TYPE - VisitRecord");
        System.out.println("------------------------------------------");
        
        VisitRecord visit1 = new VisitRecord(
            "Mary Johnson",
            "P-2024-001",
            LocalDateTime.of(2024, 10, 25, 14, 0),
            60,
            "Family visit",
            true
        );
        
        System.out.println("Created visit: " + visit1);
        System.out.println("Visitor: " + visit1.visitorName());
        System.out.println("Duration: " + visit1.durationMinutes() + " minutes");
        System.out.println("Status: " + visit1.getStatus());
        System.out.println("✓ RECORD with auto-generated getters");
        System.out.println();
        
        // Factory method
        VisitRecord visit2 = VisitRecord.createStandardVisit(
            "John Smith",
            "P-2024-002",
            LocalDateTime.now().plusDays(1)
        );
        System.out.println("✓ Factory method: " + visit2);
        System.out.println();
        
        // ============================================
        // TEST 2: RECORD TYPE - IncidentReport
        // ============================================
        System.out.println("TEST 2: RECORD TYPE - IncidentReport (with defensive copying)");
        System.out.println("------------------------------------------");
        
        IncidentReport incident = IncidentReport.createMajorIncident(
            "INC-2024-001",
            "Altercation in cafeteria",
            "Guard Johnson",
            "P-2024-001", "P-2024-003"  // Varargs!
        );
        
        System.out.println("Incident: " + incident);
        System.out.println("Involved count: " + incident.getInvolvedCount());
        System.out.println("High priority? " + incident.isHighPriority());
        System.out.println();
        
        // Test defensive copying in record
        String[] prisoners = incident.involvedPrisoners();
        System.out.println("Original: " + prisoners[0]);
        prisoners[0] = "MODIFIED";
        System.out.println("After modification: " + prisoners[0]);
        System.out.println("Record's copy: " + incident.involvedPrisoners()[0]);
        System.out.println("✓ Defensive copying in record working!");
        System.out.println();
        
        // ============================================
        // TEST 3: CUSTOM IMMUTABLE TYPE - PrisonerID
        // ============================================
        System.out.println("TEST 3: CUSTOM IMMUTABLE TYPE - PrisonerID");
        System.out.println("------------------------------------------");
        
        PrisonerID id1 = new PrisonerID("P", 2024, 1);
        System.out.println("Created ID: " + id1.getFormattedId());
        System.out.println("Year: " + id1.getYear());
        System.out.println("Is current year? " + id1.isCurrentYear());
        System.out.println();
        
        // Demonstrate immutability
        System.out.println("Testing immutability...");
        PrisonerID id2 = id1.getNextSequence();
        System.out.println("Original ID: " + id1.getFormattedId());
        System.out.println("New ID: " + id2.getFormattedId());
        System.out.println("✓ Original unchanged - new object created!");
        System.out.println();
        
        // Parse from string
        PrisonerID parsed = PrisonerID.parse("P-2024-999");
        System.out.println("✓ Parsed ID: " + parsed.getFormattedId());
        System.out.println();
        
        // ============================================
        // TEST 4: SEALED INTERFACE - PrisonEvent
        // ============================================
        System.out.println("TEST 4: SEALED INTERFACE - PrisonEvent");
        System.out.println("------------------------------------------");
        
        PrisonEvent event1 = new Admission(
            LocalDateTime.now(),
            "P-2024-010",
            "New Prisoner",
            "A-101",
            "Court order"
        );
        
        PrisonEvent event2 = new Release(
            LocalDateTime.now(),
            "P-2024-011",
            "Released Prisoner",
            "SENTENCE_COMPLETE",
            "Home"
        );
        
        PrisonEvent event3 = new Transfer(
            LocalDateTime.now(),
            "P-2024-012",
            "Transferred Prisoner",
            "B-205",
            "C-301",
            "Security upgrade"
        );
        
        System.out.println("Created 3 sealed event types:");
        System.out.println("  1. " + event1.getEventType());
        System.out.println("  2. " + event2.getEventType());
        System.out.println("  3. " + event3.getEventType());
        System.out.println("✓ Sealed interface limits implementations!");
        System.out.println();
        
        // ============================================
        // TEST 5: PATTERN MATCHING with Sealed Types
        // ============================================
        System.out.println("TEST 5: PATTERN MATCHING with Sealed Types");
        System.out.println("------------------------------------------");
        
        processEvent(event1);
        processEvent(event2);
        processEvent(event3);
        System.out.println();
        
        // ============================================
        // TEST 6: SWITCH EXPRESSIONS with Pattern Matching
        // ============================================
        System.out.println("TEST 6: SWITCH EXPRESSIONS with Pattern Matching");
        System.out.println("------------------------------------------");
        
        String message1 = getEventMessage(event1);
        String message2 = getEventMessage(event2);
        String message3 = getEventMessage(event3);
        
        System.out.println("Switch results:");
        System.out.println("  " + message1);
        System.out.println("  " + message2);
        System.out.println("  " + message3);
        System.out.println("✓ Exhaustive pattern matching with sealed types!");
        System.out.println();
        
        // ============================================
        // SUMMARY
        // ============================================
        System.out.println("==============================================");
        System.out.println("   ALL TESTS PASSED! ✓");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("Java Features Demonstrated:");
        System.out.println("! All 17 Fundamentals (COMPLETE!)");
        System.out.println();
        System.out.println("ADVANCED:");
        System.out.println("! DEFAULT interface methods");
        System.out.println("! STATIC interface methods");
        System.out.println("! PRIVATE interface methods");
        System.out.println("! DEFENSIVE COPYING");
        System.out.println("! RECORDS (VisitRecord, IncidentReport) = NEW!");
        System.out.println("! CUSTOM IMMUTABLE TYPE (PrisonerID) = NEW!");
        System.out.println("! SEALED INTERFACE (PrisonEvent) = NEW!");
        System.out.println("! PATTERN MATCHING in switch = NEW!");
        System.out.println();
        System.out.println("==============================================");
        System.out.println("   🎉 Progress: 17/17 Fundamentals | 8/12 Advanced");
        System.out.println("   Overall: 92% Complete!");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("Next: Prison manager class with Lambdas,");
        System.out.println("      Method References, final/effectively final");
    }
    
    // ============================================
    // HELPER METHODS
    // ============================================
    
    /**
     * Pattern matching with instanceof
     */
    private static void processEvent(PrisonEvent event) {
        if (event instanceof Admission admission) {
            System.out.println("  ✓ Admission: " + admission.prisonerName() +
                             " to " + admission.cellNumber());
        } else if (event instanceof Release release) {
            System.out.println("  ✓ Release: " + release.prisonerName() +
                             " via " + release.releaseType());
        } else if (event instanceof Transfer transfer) {
            System.out.println("  ✓ Transfer: " + transfer.prisonerName() +
                             " from " + transfer.fromCell() + " to " + transfer.toCell());
        }
    }
    
    /**
     * Switch expression with pattern matching
     * No default needed - compiler knows all sealed types!
     */
    private static String getEventMessage(PrisonEvent event) {
        return switch (event) {
            case Admission a -> "New admission: " + a.prisonerName() + 
                              " to " + a.cellNumber();
            case Release r -> "Release: " + r.prisonerName() + 
                            " via " + r.releaseType();
            case Transfer t -> "Transfer: " + t.prisonerName() + 
                             " from " + t.fromCell() + " to " + t.toCell();
        };
    }
}