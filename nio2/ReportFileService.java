package nio2;

import model.Prisoner;
import records.IncidentReport;
import enums.SecurityLevel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ReportFileService — demonstrates NIO2 file operations
 *
 * Analogy: This is the prison's filing cabinet system.
 * - writeString = putting a document INTO the cabinet
 * - readString  = taking a document OUT of the cabinet
 * - Files.exists = checking if a folder/drawer exists
 * - Files.createDirectories = creating a new drawer
 *
 * NIO2 (New I/O 2) is Java's modern file API introduced in Java 7.
 * It replaced the old File class with Path, which is more reliable
 * and works consistently across Windows, Mac, and Linux.
 */
public class ReportFileService {

    // Path is like an address for a file — works on ALL operating systems
    // Paths.get() builds the address from folder/file name parts
    private final Path reportsDir;
    private final Path incidentLogPath;
    private final Path prisonerReportPath;

    public ReportFileService() {
        // All reports go into a "reports" subfolder next to your src/
        this.reportsDir        = Paths.get("reports");
        this.incidentLogPath   = reportsDir.resolve("incidents.txt");
        this.prisonerReportPath = reportsDir.resolve("prisoner_report.txt");
    }

    /**
     * Demonstrates: Files.createDirectories, Files.writeString with APPEND
     *
     * StandardOpenOption.CREATE        = create file if it doesn't exist
     * StandardOpenOption.APPEND        = add to end instead of overwriting
     */
    public void logIncident(IncidentReport incident) throws IOException {
        // CREATE the reports directory if it doesn't exist yet
        // createDirectories() won't throw an error if it already exists
        Files.createDirectories(reportsDir);

        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Build the text to write
        String entry = String.format(
            "[%s] ID: %s | Severity: %s | Reporter: %s | Description: %s%n",
            timestamp,
            incident.reportId(),
            incident.severity(),
            incident.reportedBy(),
            incident.description()
        );

        // FILES.WRITESTRING — the core NIO2 write operation
        // CREATE = make the file if it doesn't exist
        // APPEND = don't overwrite, add to the end (like a log)
        Files.writeString(
            incidentLogPath,
            entry,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );

        System.out.println("  Incident logged to: " + incidentLogPath.toAbsolutePath());
    }

    /**
     * Demonstrates: Files.writeString (overwrite) and Files.readString
     */
    public void writePrisonerReport(List<Prisoner> prisoners) throws IOException {
        Files.createDirectories(reportsDir);

        // Build a full report as one big string
        StringBuilder report = new StringBuilder();
        report.append("=== PRISONER REPORT ===\n");
        report.append("Generated: ").append(LocalDateTime.now()).append("\n");
        report.append("Total prisoners: ").append(prisoners.size()).append("\n\n");

        for (Prisoner p : prisoners) {
            report.append(String.format(
                "%-15s | %-20s | Security: %-8s | Sentence: %d yrs%n",
                p.getId(), p.getName(),
                p.getSecurityLevel().name(),
                p.getSentenceYears()
            ));
        }

        // WRITE without APPEND = overwrites the file each time
        // CREATE = make it if it doesn't exist
        Files.writeString(
            prisonerReportPath,
            report.toString(),
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING  // overwrite old content
        );

        System.out.println("  Report written to: " + prisonerReportPath.toAbsolutePath());
    }

    /**
     * Demonstrates: Files.readString — read entire file into a String
     */
    public String readIncidentLog() throws IOException {
        // FILES.EXISTS — check before reading so we don't crash
        if (!Files.exists(incidentLogPath)) {
            return "  No incident log found. Log an incident first.";
        }

        // FILES.READSTRING — entire file content in one line
        return Files.readString(incidentLogPath);
    }

    public String readPrisonerReport() throws IOException {
        if (!Files.exists(prisonerReportPath)) {
            return "  No prisoner report found. Generate one first.";
        }
        return Files.readString(prisonerReportPath);
    }

    /**
     * Demonstrates: Files.exists, Files.size, Files.getLastModifiedTime
     * These are NIO2 file metadata operations
     */
    public void showFileInfo() throws IOException {
        System.out.println("\n  --- File System Info (NIO2 metadata) ---");

        showFileDetails("Incident log",    incidentLogPath);
        showFileDetails("Prisoner report", prisonerReportPath);
    }

    private void showFileDetails(String label, Path path) throws IOException {
        if (Files.exists(path)) {
            System.out.printf("  %-20s | Size: %5d bytes | Last modified: %s%n",
                label,
                Files.size(path),
                Files.getLastModifiedTime(path)
            );
        } else {
            System.out.printf("  %-20s | Does not exist yet%n", label);
        }
    }

    /**
     * Run all NIO2 demonstrations in sequence
     */
    public void runAll(List<Prisoner> prisoners) throws IOException {
        System.out.println("\n--- NIO2: Writing Files ---");

        // 1. Write prisoner report
        writePrisonerReport(prisoners);

        // 2. Log two sample incidents
        IncidentReport inc1 = IncidentReport.createMajorIncident(
            "INC-001", "Cell block disturbance", "Officer Jones",
            "P-2024-001", "P-2024-002"
        );
        IncidentReport inc2 = IncidentReport.createMinorIncident(
            "INC-002", "Contraband found", "Officer Smith", "P-2024-003"
        );

        logIncident(inc1);
        logIncident(inc2);

        System.out.println("\n--- NIO2: Reading Files ---");

        // 3. Read back the prisoner report
        System.out.println("\n  Prisoner report contents:");
        System.out.println(readPrisonerReport());

        // 4. Read back the incident log
        System.out.println("  Incident log contents:");
        System.out.println(readIncidentLog());

        // 5. Show file metadata
        showFileInfo();
    }
}