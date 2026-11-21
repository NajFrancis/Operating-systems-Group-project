import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import loggingpackage.AuditLogger;
import managerpackage.ResourceManager;
import permissionspackage.AccessController;
import permissionspackage.User;
import simulationpackage.AccessPipeline;
import userpackage.Process;

/*
 * OS Security & Resource-Access Simulator (CIT3002, Group 4) - main application.
 * Sets up users and processes, then drives the pipeline from a menu or the scripted demo.
 */
public class Main {
    private static final List<Process> processes = new ArrayList<Process>();
    private static ResourceManager resourceManager;
    private static AuditLogger auditLogger;
    private static AccessPipeline pipeline;
    private static Scanner in;

    public static void main(String[] args) {
        System.out.println("=== OS Security & Resource-Access Simulator ===");

        // Simulated users and processes (same set the group's App.java used)
        User adminUser = new User("Saiki", User.ADMIN);
        User standardUser = new User("Amy", User.USER);
        processes.add(new Process("P1", standardUser, false));
        processes.add(new Process("P2", adminUser, false));
        processes.add(new Process("P3", standardUser, false));

        resetSimulation();

        in = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option", 0, 5);
            System.out.println();
            switch (choice) {
                case 1: runDemo(); break;
                case 2: makeRequest(); break;
                case 3: releaseResource(); break;
                case 4: pipeline.displayStatus(); break;
                case 5: auditLogger.printAuditReport(); break;
                default: running = false; // 0 or end of input
            }
        }
        System.out.println("Simulation ended.");
    }

    /* Rebuilds the managers so every demo starts from a fresh state. */
    private static void resetSimulation() {
        resourceManager = new ResourceManager();
        auditLogger = new AuditLogger();
        pipeline = new AccessPipeline(resourceManager, new AccessController(), auditLogger);
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Run demo scenario (resets the simulation first)");
        System.out.println("2. Make an access request");
        System.out.println("3. Release a resource (serves the FCFS queue)");
        System.out.println("4. Show resource status and queues");
        System.out.println("5. Show audit report");
        System.out.println("0. Exit");
    }

    /* The group's four demos from App.java, plus one extra for role-based access. */
    private static void runDemo() {
        resetSimulation();
        Process p1 = processes.get(0);
        Process p2 = processes.get(1);
        Process p3 = processes.get(2);

        System.out.println("--- DEMO 1: Standard Access Allocation ---");
        pipeline.handleRequest(p1, "FileA.txt");

        System.out.println("\n--- DEMO 2: Resource Contention (FCFS Queueing) ---");
        pipeline.handleRequest(p2, "FileA.txt");
        pipeline.handleRequest(p3, "FileA.txt");

        System.out.println("\n--- DEMO 3: Security Policy Violations ---");
        pipeline.handleRequest(p1, "Registry");
        pipeline.handleRequest(p1, "KernelConfig");

        System.out.println("\n--- DEMO 4: Lock Release & Queue Servicing ---");
        pipeline.releaseAndServiceQueue("FileA.txt");
        pipeline.releaseAndServiceQueue("FileA.txt");

        System.out.println("\n--- DEMO 5: Admin Privilege and Unknown Resource ---");
        pipeline.handleRequest(p2, "Registry");
        pipeline.handleRequest(p3, "Printer.sys");

        System.out.println("\n--- Final resource status ---");
        pipeline.displayStatus();
        auditLogger.printAuditReport();
    }

    private static void makeRequest() {
        for (int i = 0; i < processes.size(); i++) {
            System.out.print((i + 1) + ". ");
            processes.get(i).display();
        }
        int p = readInt("Process", 1, processes.size());
        if (p < 1) return;

        List<String> names = resourceNames();
        for (int i = 0; i < names.size(); i++) {
            System.out.println((i + 1) + ". " + names.get(i));
        }
        int r = readInt("Resource", 1, names.size());
        if (r < 1) return;

        pipeline.handleRequest(processes.get(p - 1), names.get(r - 1));
    }

    private static void releaseResource() {
        List<String> names = resourceNames();
        for (int i = 0; i < names.size(); i++) {
            System.out.println((i + 1) + ". " + names.get(i));
        }
        int r = readInt("Resource to release", 1, names.size());
        if (r < 1) return;

        pipeline.releaseAndServiceQueue(names.get(r - 1));
    }

    private static List<String> resourceNames() {
        return new ArrayList<String>(resourceManager.getResourceMap().keySet());
    }

    /* Reads an int in [min, max]. Returns -1 if input ends. */
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " [" + min + "-" + max + "]: ");
            if (!in.hasNextLine()) return -1;
            String line = in.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException e) {
                // fall through to the error message
            }
            System.out.println("Invalid input, try again.");
        }
    }
}
