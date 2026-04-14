package concurrency;

import model.Cell;
import model.Prisoner;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CellProcessingService {

    private final List<Cell> cells;
    private final List<Prisoner> prisoners;

    public CellProcessingService(List<Cell> cells, List<Prisoner> prisoners) {
        this.cells = cells;
        this.prisoners = prisoners;
    }

    /**
     * Demonstrates: ExecutorService + List of Callables
     *
     * Callable<T> is like Runnable but:
     *   - it RETURNS a value (type T)
     *   - it CAN throw a checked exception
     *
     * Future<T> is the "receipt" for a Callable task:
     *   - future.get() blocks until the result is ready
     */
    public void runCellSecurityCheck() throws InterruptedException {
        System.out.println("\n--- Concurrency: Cell Security Check ---");
        System.out.println("Spawning thread pool with 3 agents...\n");

        // Step 1: Create a thread pool with 3 worker threads
        // Analogy: hiring 3 security agents to check cells in parallel
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Step 2: Build a list of Callable tasks — one per cell
        // Each Callable simulates inspecting a cell and returns a report string
        // Stream + lambda converts each Cell into a Callable<String>
        List<Callable<String>> tasks = cells.stream()
            .map(cell -> (Callable<String>) () -> {
                // Simulate inspection work (small delay to show concurrency)
                Thread.sleep(100);

                long occupants = cell.getOccupantCount();
                String status  = cell.isAvailable() ? "AVAILABLE" : "FULL";
                String risk    = cell.getSecurityLevel().name();

                return String.format(
                    "Cell %-6s | Security: %-7s | Occupants: %d | Status: %s  [Thread: %s]",
                    cell.getCellNumber(), risk, occupants, status,
                    Thread.currentThread().getName()
                );
            })
            .toList();

        // Step 3: Submit ALL tasks at once — executor assigns them to threads
        // invokeAll() blocks until every task is finished
        // Returns a List<Future<String>> — one Future per task
        List<Future<String>> results = executor.invokeAll(tasks);

        // Step 4: Collect and print each result
        System.out.println("Security check results:");
        for (Future<String> future : results) {
            try {
                // future.get() retrieves the return value from the Callable
                System.out.println("  " + future.get());
            } catch (Exception e) {
                System.out.println("  [ERROR] Task failed: " + e.getMessage());
            }
        }

        // Step 5: ALWAYS shut down the executor when done
        // Otherwise the threads keep running and the JVM never exits
        executor.shutdown();
        System.out.println("\nExecutor shut down. All threads complete.");
    }

    /**
     * Demonstrates: Callable returning prisoner risk assessment
     * Shows Callable can return any type — here a structured result
     */
    public void runPrisonerRiskAssessment() throws InterruptedException {
        System.out.println("\n--- Concurrency: Parallel Risk Assessment ---");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Each task assesses one prisoner's risk level
        List<Callable<String>> tasks = prisoners.stream()
            .map(prisoner -> (Callable<String>) () -> {
                Thread.sleep(80);

                String risk = switch (prisoner.getSecurityLevel()) {
                    case MAXIMUM -> "CRITICAL";
                    case HIGH    -> "HIGH";
                    case MEDIUM  -> "MEDIUM";
                    case LOW     -> "LOW";
                };

                return String.format(
                    "%-15s | Risk: %-8s | Sentence: %2d yrs | Eligible release: %s",
                    prisoner.getName(), risk,
                    prisoner.getSentenceYears(),
                    prisoner.isEligibleForEarlyRelease() ? "YES" : "NO"
                );
            })
            .toList();

        List<Future<String>> results = executor.invokeAll(tasks);

        System.out.println("Risk assessment results:");
        for (Future<String> future : results) {
            try {
                System.out.println("  " + future.get());
            } catch (Exception e) {
                System.out.println("  [ERROR] " + e.getMessage());
            }
        }

        executor.shutdown();
        System.out.println("\nAll risk assessments complete.");
    }

    /**
     * Run both demonstrations together
     */
    public void runAll() throws InterruptedException {
        runCellSecurityCheck();
        runPrisonerRiskAssessment();
    }
}