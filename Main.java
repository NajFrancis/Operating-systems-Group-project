import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import loggingpackage.AuditLogger;
import managerpackage.ResourceManager;
import permissionspackage.AccessController;
import permissionspackage.User;
import simulationpackage.AccessPipeline;
import simulationpackage.UserBridge;

/*
 * OS Security & Resource-Access Simulator (CIT3002, Group 4) - terminal interface.
 * Login/registration is member 4's (member4.User); everything after login goes through
 * AccessPipeline, which uses the permission, resource, queue and logging classes.
 */
public class Main {
    private static ResourceManager resourceManager;
    private static AuditLogger auditLogger;
    private static AccessPipeline pipeline;
    private static Scanner in;

    public static void main(String[] args) {
        System.out.println("*********Group 4 OS Security Presentation**********");

        // member4.User reads and writes these two files
        try {
            new File("password.txt").createNewFile();
            new File("group.txt").createNewFile();
        } catch (IOException e) {
            System.out.println("Unable to create files.");
            return;
        }

        member4.User auth = new member4.User();
        in = UserBridge.scannerOf(auth);
        resetSimulation();

        try {
            User current = authenticate(auth);
            menu(current);
        } catch (NoSuchElementException e) {
            System.out.println("\nInput ended.");
        }
        System.out.println("Simulation ended.");
    }

    /* Rebuilds the managers so a demo starts from a clean state. */
    private static void resetSimulation() {
        resourceManager = new ResourceManager();
        auditLogger = new AuditLogger();
        pipeline = new AccessPipeline(resourceManager, new AccessController(), auditLogger);
    }

    // ---------- authentication (member 4's register/login) ----------

    private static User authenticate(member4.User auth) {
        while (true) {
            System.out.println("\nAre you a first-time user? (Yes/No)");
            String answer = in.nextLine().trim();

            boolean loggedIn;
            if (answer.equalsIgnoreCase("Yes")) {
                auth.register();
                loggedIn = true;
            } else if (answer.equalsIgnoreCase("No")) {
                loggedIn = auth.login();
            } else {
                System.out.println("Invalid selection. Enter Yes or No.");
                continue;
            }
            if (loggedIn) return toPermissionUser(auth);
        }
    }

    /* Converts member 4's user (group "Admin"/"Standard") to the permissions package's User (role). */
    private static User toPermissionUser(member4.User auth) {
        String name = UserBridge.usernameOf(auth);
        String group = UserBridge.groupOf(auth);
        if ("Admin".equalsIgnoreCase(group)) {
            return new User(name, User.ADMIN);
        }
        if (group == null) {
            System.out.println("[SYSTEM NOTICE] No group found for " + name + "; treating as a standard user.");
        }
        return new User(name, User.USER);
    }

    // ---------- main menu ----------

    private static void menu(User current) {
        int option;
        do {
            System.out.println("\n*******MAIN MENU********");
            System.out.println("Logged in as " + current.getUsername() + " [" + current.getRole() + "]");
            System.out.println("1. Request a resource");
            System.out.println("2. Release a resource (then serve the waiting queue)");
            System.out.println("3. View resource status");
            System.out.println("4. View waiting and ready queues");
            System.out.println("5. View audit report");
            System.out.println("6. Run demo scenario (resets the simulation first)");
            System.out.println("7. Exit");
            option = readInt("Select an option", 1, 7);
            System.out.println();

            switch (option) {
                case 1: requestResource(current); break;
                case 2: releaseResource(); break;
                case 3: pipeline.displayStatus(); break;
                case 4: pipeline.displayQueues(); break;
                case 5: auditLogger.printAuditReport(); break;
                case 6: runDemo(); break;
                default: break; // 7, or end of input
            }
        } while (option != 7 && option != -1);

        System.out.println("Logged out successfully!");
    }

    private static void requestResource(User current) {
        List<String> names = resourceNames();
        int r = chooseResource(names, "Resource to request");
        if (r < 1) return;
        pipeline.submitRequest(current, names.get(r - 1));
    }

    private static void releaseResource() {
        List<String> names = resourceNames();
        int r = chooseResource(names, "Resource to release");
        if (r < 1) return;
        pipeline.releaseAndService(names.get(r - 1));
    }

    private static int chooseResource(List<String> names, String prompt) {
        for (int i = 0; i < names.size(); i++) {
            System.out.println((i + 1) + ". " + names.get(i));
        }
        return readInt(prompt, 1, names.size());
    }

    /* Built-in users (not logged in) so the demo always shows both an admin and a standard user. */
    private static void runDemo() {
        resetSimulation();
        User admin = new User("Saiki", User.ADMIN);
        User amy = new User("Amy", User.USER);

        System.out.println("--- DEMO 1: Standard Access Allocation ---");
        pipeline.submitRequest(amy, "FileA.txt");

        System.out.println("\n--- DEMO 2: Resource Contention (FCFS Queueing) ---");
        pipeline.submitRequest(admin, "FileA.txt");
        pipeline.submitRequest(amy, "FileA.txt");

        System.out.println("\n--- DEMO 3: Security Policy Violations ---");
        pipeline.submitRequest(amy, "Registry");
        pipeline.submitRequest(amy, "KernelConfig");

        System.out.println("\n--- DEMO 4: Lock Release & Queue Servicing ---");
        pipeline.releaseAndService("FileA.txt");

        System.out.println("\n--- DEMO 5: Admin Privilege ---");
        pipeline.submitRequest(admin, "Registry");

        System.out.println("\n--- Final resource status ---");
        pipeline.displayStatus();
        System.out.println();
        pipeline.displayQueues();
        auditLogger.printAuditReport();
    }

    // ---------- helpers ----------

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
