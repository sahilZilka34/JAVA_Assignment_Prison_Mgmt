import manager.PrisonMenu;

/**
 * Main.java — Entry point for Prison Management System
 *
 * Demonstrates:
 * - JEP 512: Instance main method (Java 25 Preview)
 * - JEP 464: Scoped Values (Java 25 Preview) — BONUS
 */
public class Main {

    // ============================================
    // JEP 464 — Scoped Values (Java 25 Preview) BONUS
    // ============================================
    // ScopedValue is like a read-only wristband attached to a thread scope.
    // Declare it as a static final — it's a key, not the value itself.
    // The actual value is bound per-scope using ScopedValue.where(...).run(...)
    static final ScopedValue<String> CURRENT_OFFICER = ScopedValue.newInstance();
    static final ScopedValue<String> SESSION_ID      = ScopedValue.newInstance();

    // ============================================
    // JEP 512 — Instance main method (Java 25 Preview)
    // ============================================
    void main() {
        System.out.println("[JEP 512] Instance main method running...");
        launchWithScope();
    }

    // ============================================
    // Traditional static main (takes priority)
    // ============================================
    public static void main(String[] args) {
        System.out.println("[JEP 512] Traditional static main running...");
        System.out.println("[JEP 512] Instance main also declared in this class");
        System.out.println("[JEP 512] Static takes priority - both styles shown\n");
        launchWithScope();
    }

    /**
     * Wraps the entire application in a ScopedValue context.
     *
     * ScopedValue.where(KEY, value)   — binds a value to the key
     *            .run(() -> ...)       — executes code with that binding active
     *
     * Inside the run() block, CURRENT_OFFICER.get() returns "Duty Officer"
     * Outside it, calling .get() would throw NoSuchElementException
     * — the value only exists within the scope boundary
     */
    private static void launchWithScope() {
        String officer   = "Officer Murphy";
        String sessionId = "SESSION-" + System.currentTimeMillis();

        System.out.println("[Scoped Values] Binding session context...");
        System.out.println("[Scoped Values] Officer  : " + officer);
        System.out.println("[Scoped Values] SessionID: " + sessionId);
        System.out.println("[Scoped Values] Launching application within scope...\n");

        // Bind BOTH scoped values and run the entire app inside the scope
        // Any method called from here can read these values via .get()
        // without them being passed as parameters
        ScopedValue.where(CURRENT_OFFICER, officer)
                   .where(SESSION_ID, sessionId)
                   .run(() -> {
                       // Prove the values are accessible deep inside the scope
                       System.out.println("[Scoped Values] Inside scope:");
                       System.out.println("  CURRENT_OFFICER.get() = "
                           + CURRENT_OFFICER.get());
                       System.out.println("  SESSION_ID.get()      = "
                           + SESSION_ID.get());
                       System.out.println("  Values readable anywhere"
                           + " inside this scope - no parameter passing needed\n");

                       // Launch the actual application
                       new PrisonMenu().start();
                   });

        // Outside the scope — values are gone
        System.out.println("\n[Scoped Values] Outside scope:");
        System.out.println("  CURRENT_OFFICER.isBound() = "
            + CURRENT_OFFICER.isBound());
        System.out.println("  SESSION_ID.isBound()      = "
            + SESSION_ID.isBound());
        System.out.println("  Values automatically cleaned up - no leaks");
    }
}