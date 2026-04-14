import manager.PrisonMenu;

/**
 * Main.java — Entry point for Prison Management System
 *
 * Demonstrates TWO entry point styles:
 *
 * 1. Traditional static main (Java 1.0+)
 *    - Must be public static void main(String[] args)
 *    - Verbose but universally understood
 *
 * 2. Instance main method — JEP 512 (Java 25 Preview)
 *    - Just: void main()
 *    - No public, no static, no String[] args required
 *    - Java 25 launches this if no static main is found
 *    - Makes Java more accessible and less ceremonial
 */
public class Main {

    // ============================================
    // JEP 512 — Instance main method (Java 25 Preview)
    // ============================================
    // This is the new Java 25 way — minimal, clean
    // Java looks for this if no traditional static main exists
    // Note: both can coexist — static main takes priority
    void main() {
        System.out.println("[JEP 512] Instance main method running...");
        new PrisonMenu().start();
    }

    // ============================================
    // Traditional static main (kept for compatibility)
    // ============================================
    // This takes priority over the instance main above
    // Remove this method and Java 25 will use void main() instead
    public static void main(String[] args) {
        System.out.println("[JEP 512] Traditional static main running...");
        System.out.println("[JEP 512] Instance main also declared in this class");
        System.out.println("[JEP 512] Static takes priority — both styles shown\n");
        new PrisonMenu().start();
    }
}