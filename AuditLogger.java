// Andrew Ramsaran (ID: 2401428)
// Group 4
// Role: Security violation logging 
// AuditLogger.java


package loggingpackage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
 * Logging & Security Monitoring Module
 * Responsible for recording resource access requests, tracking security violations,
 * calculating request metrics, and printing system audit reports.
 */
public class AuditLogger {
    private List<String> logs;
    private int totalRequests;
    private int violationsCount;
    private int queuedCount;

    /*
     * Constructor for AuditLogger.
     * Initializes the log storage list and resets system metrics counters.
     */
    public AuditLogger() {
        this.logs = new ArrayList<>();
        this.totalRequests = 0;
        this.violationsCount = 0;
        this.queuedCount = 0;
    }

    /*
     * Intercepts access request results, increments metric counters,
     * formats timestamped event entries, and records log history.
     */
    public void logAccessRequest(String processId, String username, String resourceName, String status, String details) {
        this.totalRequests++;
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (status.contains("VIOLATION") || status.contains("DENIED")) {
            this.violationsCount++;
        } else if (status.contains("QUEUED")) {
            this.queuedCount++;
        }

        String logEntry = String.format("[%s] Process '%s' (User: %s) -> Resource '%s' | Status: %-25s | Notes: %s",
                timestamp, processId, username, resourceName, status, details);
        this.logs.add(logEntry);
        System.out.println(logEntry);
    }

    /*
     * Generates and outputs the final System Audit & Security Report.
     * Displays summary statistics and chronologically lists recorded event logs.
     */
    public void printAuditReport() {
        System.out.println("\n==========================================================================");
        System.out.println("                   SYSTEM AUDIT & SECURITY REPORT                         ");
        System.out.println("==========================================================================");
        System.out.println("Total Access Requests Processed : " + this.totalRequests);
        System.out.println("Total Security Violations       : " + this.violationsCount);
        System.out.println("Total Requests Placed in Queue  : " + this.queuedCount);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Detailed Event Logs:");
        for (String log : this.logs) {
            System.out.println("  " + log);
        }
        System.out.println("==========================================================================\n");
    }

    /*
     * Returns the total number of access requests processed.
     */
    public int getTotalRequests() {
        return this.totalRequests;
    }

    /*
     * Returns the total number of security violations detected.
     */
    public int getViolationsCount() {
        return this.violationsCount;
    }

    /*
     * Returns the total number of requests placed in the FCFS waiting queue.
     */
    public int getQueuedCount() {
        return this.queuedCount;
    }
}