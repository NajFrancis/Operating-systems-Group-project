package loggingpackage;
// Andrew Ramsaran (ID: 2401428)
// Group 4
// Role: Security violation logging 
// Audit.java



import managerpackage.ResourceManager;
import resourcepackage.Resource;
import userpackage.User;
import userpackage.Process;
import securitypackage.AccessController;

/*
 * Main Application Driver
 * Integrates resource management, user authentication, access control, 
 * and security monitoring modules into a unified OS access pipeline.
 */
public class App {

    /*
     * Application entry point. Executes system initialization, 
     * request processing tests, resource release procedures, and report generation.
     */
    public static void main(String[] args) {
        System.out.println("=== OS Security & Resource-Access Simulator ===\n");

        // Initialize core manager objects
        ResourceManager resourceManager = new ResourceManager();
        AccessController accessController = new AccessController();
        AuditLogger auditLogger = new AuditLogger();

        // Instantiate simulated users and processes
        User adminUser = new User("Saiki", User.ADMIN);
        User standardUser = new User("Amy", User.USER);

        Process process1 = new Process("P1", standardUser, false);
        Process process2 = new Process("P2", adminUser, false);
        Process process3 = new Process("P3", standardUser, false);

        // Executing request simulation workflow
        System.out.println("--- DEMO 1: Standard Access Allocation ---");
        handleRequest(process1, "FileA.txt", resourceManager, accessController, auditLogger);

        System.out.println("\n--- DEMO 2: Resource Contention (FCFS Queueing) ---");
        handleRequest(process2, "FileA.txt", resourceManager, accessController, auditLogger);
        handleRequest(process3, "FileA.txt", resourceManager, accessController, auditLogger);

        System.out.println("\n--- DEMO 3: Security Policy Violations ---");
        handleRequest(process1, "Registry", resourceManager, accessController, auditLogger);
        handleRequest(process1, "KernelConfig", resourceManager, accessController, auditLogger);

        System.out.println("\n--- DEMO 4: Lock Release & Queue Servicing ---");
        releaseAndServiceQueue("FileA.txt", resourceManager, auditLogger);
        releaseAndServiceQueue("FileA.txt", resourceManager, auditLogger);

        // Output final audit and monitoring summary
        auditLogger.printAuditReport();
    }

    /*
     * Pipeline processing function:
     * Request -> Permission Check -> Allocation / Queueing -> Security Logging
     */
    private static void handleRequest(Process process, String resourceName,
                                       ResourceManager resMgr,
                                       AccessController accessCtrl,
                                       AuditLogger logger) {
        Resource target = resMgr.getResource(resourceName);
        if (target == null) {
            logger.logAccessRequest(process.getProcessId(), process.getOwner().getUsername(), resourceName, "NOT_FOUND", "Resource missing from system directory");
            return;
        }

        // Permission verification
        boolean isAllowed = accessCtrl.requestAccess(process, target);

        if (!isAllowed) {
            logger.logAccessRequest(process.getProcessId(), process.getOwner().getUsername(), resourceName, "DENIED (SECURITY VIOLATION)", "Access denied by permission policy");
        } else {
            if (target.isAvailable()) {
                target.allocate(process.getProcessId());
                logger.logAccessRequest(process.getProcessId(), process.getOwner().getUsername(), resourceName, "ALLOWED (ALLOCATED)", "Resource lock acquired successfully");
            } else {
                target.addToWaitingQueue(process);
                logger.logAccessRequest(process.getProcessId(), process.getOwner().getUsername(), resourceName, "QUEUED (FCFS)", "Resource busy -> Placed in FCFS waiting queue");
            }
        }
    }

    /*
     * Unlocks a busy resource and grants lock access to the next process in the FCFS queue.
     */
    private static void releaseAndServiceQueue(String resourceName, ResourceManager resMgr, AuditLogger logger) {
        Resource target = resMgr.getResource(resourceName);
        if (target != null && target.getInUse()) {
            String previousOwner = target.getCurrentOwnerProcess();
            target.release();

            if (target.hasWaitingProcesses()) {
                Process nextProcess = target.getNextWaitingProcess();
                target.allocate(nextProcess.getProcessId());
                logger.logAccessRequest(nextProcess.getProcessId(), nextProcess.getOwner().getUsername(),
                                        resourceName, "ALLOWED (DEQUEUED & ALLOCATED)", "Granted access from FCFS queue after Process " + previousOwner + " released lock");
            } else {
                System.out.println("[SYSTEM NOTICE] Queue empty. " + resourceName + " is now available.");
            }
        }
    }
}