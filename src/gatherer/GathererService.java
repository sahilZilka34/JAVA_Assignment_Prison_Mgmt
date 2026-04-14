package gatherer;

import model.Prisoner;

import java.util.List;
import java.util.stream.Gatherers;

/**
 * GathererService — demonstrates Stream Gatherers (Java 25 Preview) BONUS
 *
 * Gatherers are CUSTOM intermediate stream operations.
 * Built-in intermediates (filter, map, sorted) cover most needs.
 * Gatherers fill the gaps for anything custom.
 *
 * Java 25 ships with four built-in Gatherers:
 *   windowFixed(n)     — groups elements into fixed-size windows
 *   windowSliding(n)   — sliding window (overlapping groups)
 *   fold(...)          — custom reduction
 *   scan(...)          — running total / accumulation
 */
public class GathererService {

    private final List<Prisoner> prisoners;

    public GathererService(List<Prisoner> prisoners) {
        this.prisoners = prisoners;
    }

    /**
     * windowFixed(n) — splits the stream into non-overlapping
     * groups of exactly n elements.
     *
     * Analogy: dividing prisoners into patrol groups of 2.
     * [A, B, C, D, E, F] → [[A,B], [C,D], [E,F]]
     */
    public void demonstrateWindowFixed() {
        System.out.println("\n--- Gatherers.windowFixed(2) ---");
        System.out.println("  Grouping prisoners into patrol groups of 2:\n");

        List<List<Prisoner>> groups = prisoners.stream()
            .gather(Gatherers.windowFixed(2))
            .toList();

        int groupNum = 1;
        for (List<Prisoner> group : groups) {
            System.out.print("  Group " + groupNum++ + ": ");
            group.forEach(p -> System.out.print(p.getName() + "  "));
            System.out.println();
        }
    }

    /**
     * windowSliding(n) — overlapping windows of n elements.
     *
     * Analogy: a security camera that always watches
     * the last 2 prisoners who walked past.
     * [A, B, C, D] → [[A,B], [B,C], [C,D]]
     */
    public void demonstrateWindowSliding() {
        System.out.println("\n--- Gatherers.windowSliding(2) ---");
        System.out.println("  Sliding window of 2 (overlapping pairs):\n");

        List<List<Prisoner>> windows = prisoners.stream()
            .gather(Gatherers.windowSliding(2))
            .toList();

        for (List<Prisoner> window : windows) {
            System.out.print("  Window: ");
            window.forEach(p -> System.out.print(p.getName() + "  "));
            System.out.println();
        }
    }

    /**
     * scan() — running accumulation, like a running total.
     *
     * Analogy: a tally counter that keeps a running sentence total
     * as it processes each prisoner.
     * Sentences [10, 25, 3] → running totals [10, 35, 38]
     */
    public void demonstrateScan() {
        System.out.println("\n--- Gatherers.scan() - running sentence total ---");
        System.out.println("  Accumulating total sentence years:\n");

        List<Integer> runningTotals = prisoners.stream()
            .map(Prisoner::getSentenceYears)
            .gather(Gatherers.scan(() -> 0, Integer::sum))
            .toList();

        List<Prisoner> prisonerList = prisoners;
        for (int i = 0; i < prisonerList.size(); i++) {
            System.out.printf("  After %-15s (+ %2d yrs) running total: %d yrs%n",
                prisonerList.get(i).getName(),
                prisonerList.get(i).getSentenceYears(),
                runningTotals.get(i)
            );
        }
    }

    /**
     * Run all Gatherer demonstrations
     */
    public void runAll() {
        demonstrateWindowFixed();
        demonstrateWindowSliding();
        demonstrateScan();
    }
}