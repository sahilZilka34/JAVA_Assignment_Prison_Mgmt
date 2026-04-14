package analytics;

import model.Prisoner;
import enums.SecurityLevel;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * PrisonAnalyticsService — demonstrates ALL required Stream operations
 *
 * Analogy: This class is a "reporting department" that runs queries
 * against the prisoner database without ever modifying it.
 */
public class PrisonAnalyticsService {

    private final List<Prisoner> prisoners;

    public PrisonAnalyticsService(List<Prisoner> prisoners) {
        // Defensive copy — we never mutate the original list
        this.prisoners = List.copyOf(prisoners);
    }

    // =========================================================
    // FIX 7 — LAMBDAS: Explicit Consumer, Function, Supplier
    // (Predicate already exists in Prison.java — these are the
    //  three that were missing)
    // =========================================================

    // Consumer<T>   — takes a T, returns nothing. Used for side effects.
    // Analogy: a printer — you hand it a document, it prints, gives nothing back
    private final Consumer<Prisoner> printPrisoner =
        p -> System.out.println("  [Consumer] " + p.getName()
                + " | " + p.getSecurityLevel()
                + " | " + p.getSentenceYears() + " yrs");

    // Function<T, R> — takes a T, returns an R. Used for transformations.
    // Analogy: a scanner — you hand it a document, it gives you a digital copy
    private final Function<Prisoner, String> prisonerSummary =
        p -> p.getName() + " (" + p.getId() + ") — " + p.getSentenceYears() + " years";

    // Supplier<T>   — takes nothing, returns a T. Used for generating values.
    // Analogy: a ticket machine — you press a button, it gives you a number
    private final Supplier<String> reportHeader =
        () -> "=== Prison Analytics Report [" + java.time.LocalDateTime.now() + "] ===";

    // Predicate<T>  — takes a T, returns boolean. (Already in Prison.java, shown here for completeness)
    private final Predicate<Prisoner> isHighRisk =
        p -> p.getSecurityLevel() == SecurityLevel.HIGH
          || p.getSecurityLevel() == SecurityLevel.MAXIMUM;

    // =========================================================
    // FIX 3 — STREAMS: Terminal operations
    // =========================================================

    /**
     * Demonstrates: count(), min(), max(), findFirst(), findAny()
     */
    public void runTerminalOperations() {
        System.out.println("\n" + reportHeader.get()); // Supplier in action
        System.out.println("\n--- Terminal Operations ---");

        // count() — how many prisoners total?
        long total = prisoners.stream()
                .count();
        System.out.println("count()      total prisoners: " + total);

        // count() with filter — how many high-risk?
        long highRisk = prisoners.stream()
                .filter(isHighRisk)   // Predicate reused here
                .count();
        System.out.println("filter+count high risk: " + highRisk);

        // min() — shortest sentence (needs a Comparator)
        // Returns Optional because the list might be empty
        Optional<Prisoner> shortestSentence = prisoners.stream()
                .min(Comparator.comparing(Prisoner::getSentenceYears));
        shortestSentence.ifPresent(p ->
                System.out.println("min()        shortest sentence: "
                        + p.getName() + " (" + p.getSentenceYears() + " yrs)"));

        // max() — longest sentence
        Optional<Prisoner> longestSentence = prisoners.stream()
                .max(Comparator.comparing(Prisoner::getSentenceYears));
        longestSentence.ifPresent(p ->
                System.out.println("max()        longest sentence: "
                        + p.getName() + " (" + p.getSentenceYears() + " yrs)"));

        // findFirst() — first prisoner in the list (deterministic)
        Optional<Prisoner> first = prisoners.stream()
                .findFirst();
        first.ifPresent(p ->
                System.out.println("findFirst()  first prisoner: " + p.getName()));

        // findAny() — any prisoner (faster in parallel streams, same as findFirst here)
        Optional<Prisoner> any = prisoners.stream()
                .filter(p -> p.getSentenceYears() > 5)
                .findAny();
        any.ifPresent(p ->
                System.out.println("findAny()    any with >5yr sentence: " + p.getName()));

        // allMatch() — do ALL prisoners have a valid ID?
        boolean allValid = prisoners.stream()
                .allMatch(Prisoner::isValidId);
        System.out.println("allMatch()   all valid IDs: " + allValid);

        // anyMatch() — is ANYONE eligible for early release?
        boolean anyEligible = prisoners.stream()
                .anyMatch(Prisoner::isEligibleForEarlyRelease);
        System.out.println("anyMatch()   any eligible for release: " + anyEligible);

        // noneMatch() — is nobody serving more than 50 years?
        boolean noneOver50 = prisoners.stream()
                .noneMatch(p -> p.getSentenceYears() > 50);
        System.out.println("noneMatch()  none serving >50 years: " + noneOver50);

        // forEach() — print each prisoner using our Consumer
        System.out.println("forEach()    all prisoners:");
        prisoners.stream()
                .forEach(printPrisoner);  // Consumer in action
    }

    // =========================================================
    // FIX 4 — STREAMS: collect() with three collectors
    // =========================================================

    /**
     * Demonstrates: groupingBy(), partitioningBy(), toMap()
     *
     * Think of collectors as "buckets" at the end of the pipeline:
     *   groupingBy    → multiple labelled buckets (Map<Key, List<V>>)
     *   partitioningBy → exactly two buckets: true and false (Map<Boolean, List<V>>)
     *   toMap         → key-value pairs (Map<K, V>)
     */
    public void runCollectors() {
        System.out.println("\n--- Collectors ---");

        // groupingBy() — group prisoners by their security level
        // Result: { LOW=[...], MEDIUM=[...], HIGH=[...], MAXIMUM=[...] }
        Map<SecurityLevel, List<Prisoner>> bySecurityLevel = prisoners.stream()
                .collect(Collectors.groupingBy(Prisoner::getSecurityLevel));

        System.out.println("groupingBy(SecurityLevel):");
        bySecurityLevel.forEach((level, group) ->
                System.out.println("  " + level + ": " + group.size() + " prisoner(s)"));

        // partitioningBy() — split into exactly two groups: eligible vs not eligible
        // Result: { true=[eligible prisoners], false=[not eligible] }
        Map<Boolean, List<Prisoner>> byEligibility = prisoners.stream()
                .collect(Collectors.partitioningBy(Prisoner::isEligibleForEarlyRelease));

        System.out.println("partitioningBy(earlyRelease):");
        System.out.println("  Eligible:     " + byEligibility.get(true).size());
        System.out.println("  Not eligible: " + byEligibility.get(false).size());

        // toMap() — build a lookup map: prisoner ID → prisoner name
        // Useful for fast lookups by ID without looping
        Map<String, String> idToName = prisoners.stream()
                .collect(Collectors.toMap(
                        Prisoner::getId,    // key extractor
                        Prisoner::getName   // value extractor
                ));

        System.out.println("toMap(id -> name):");
        idToName.forEach((id, name) ->
                System.out.println("  " + id + " -> " + name));

        // Bonus: Function used with Collectors.toMap for summary strings
        System.out.println("toMap with Function (id -> summary):");
        Map<String, String> idToSummary = prisoners.stream()
                .collect(Collectors.toMap(
                        Prisoner::getId,
                        prisonerSummary  // Function<Prisoner, String> in action
                ));
        idToSummary.forEach((id, summary) ->
                System.out.println("  " + id + " -> " + summary));
    }

    // =========================================================
    // FIX 5 — STREAMS: Intermediate operations chain
    // =========================================================

    /**
     * Demonstrates: filter(), map(), distinct(), sorted(), limit()
     * All chained together in one pipeline.
     */
    public void runIntermediateOperations() {
        System.out.println("\n--- Intermediate Operations ---");

        // Full chain: filter → map → distinct → sorted → limit
        //
        // Read this like a sentence:
        // "From all prisoners, keep those with >2yr sentences,
        //  extract their crime types, remove duplicates,
        //  sort alphabetically, take only the first 5"
        List<String> topCrimes = prisoners.stream()
                .filter(p -> p.getSentenceYears() > 2)          // intermediate: filter
                .map(Prisoner::getCrimeType)                     // intermediate: map (Prisoner → String)
                .distinct()                                      // intermediate: remove duplicates
                .sorted()                                        // intermediate: alphabetical order
                .limit(5)                                        // intermediate: take max 5
                .toList();                                       // terminal: collect to List

        System.out.println("filter+map+distinct+sorted+limit:");
        topCrimes.forEach(crime -> System.out.println("  " + crime));

        // map() to transform objects — Prisoner → summary String
        System.out.println("\nmap() Prisoner -> summary string:");
        prisoners.stream()
                .map(prisonerSummary)                            // Function reused here
                .forEach(s -> System.out.println("  " + s));

        // sorted() with custom Comparator
        System.out.println("\nsorted() by sentence descending:");
        prisoners.stream()
                .sorted(Comparator.comparing(Prisoner::getSentenceYears).reversed())
                .limit(3)
                .forEach(printPrisoner);                         // Consumer reused here
    }

    /**
     * Convenience method — run all demonstrations in sequence.
     * Call this from PrisonMenu option.
     */
    public void runAllAnalytics() {
        runTerminalOperations();
        runCollectors();
        runIntermediateOperations();
        System.out.println("\n=== Analytics Complete ===\n");
    }
}