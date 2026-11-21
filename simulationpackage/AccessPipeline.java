package simulationpackage;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import loggingpackage.AuditLogger;
import managerpackage.ResourceManager;
import permissionspackage.AccessController;
import resourcepackage.Resource;
import userpackage.Process;

/*
 * Access pipeline: Request -> Permission Check -> Allocation / Queueing -> Security Logging.
 * This holds the logic that was written inside App.java (handleRequest and
 * releaseAndServiceQueue), rewritten to work with the classes as they actually exist.
 *
 * Resource has no owner or waiting-queue support, so this class keeps that state itself:
 * the lock itself is still taken and released through ResourceManager.
 */
public class AccessPipeline {
    private final ResourceManager resourceManager;
    private final AccessController accessController;
    private final AuditLogger logger;

    // resource name -> id of the process holding it
    private final Map<String, String> owners = new HashMap<String, String>();
    // resource name -> FCFS list of processes waiting for it
    private final Map<String, Queue<Process>> waitingQueues = new HashMap<String, Queue<Process>>();

    public AccessPipeline(ResourceManager resourceManager, AccessController accessController, AuditLogger logger) {
        this.resourceManager = resourceManager;
        this.accessController = accessController;
        this.logger = logger;
    }

    /* Runs one access request through the full pipeline. */
    public void handleRequest(Process process, String resourceName) {
        String pid = process.getProcessId();
        String user = process.getOwner().getUsername();

        // getResourceMap().get() avoids the debug printing that ResourceManager.getResource() does
        Resource target = resourceManager.getResourceMap().get(resourceName);
        if (target == null) {
            logger.logAccessRequest(pid, user, resourceName, "NOT_FOUND",
                    "Resource missing from system directory");
            return;
        }

        // AccessController takes a User, so the process owner is what gets checked
        if (!accessController.requestAccess(process.getOwner(), target)) {
            logger.logAccessRequest(pid, user, resourceName, "DENIED (SECURITY VIOLATION)",
                    "Access denied by permission policy");
            return;
        }

        if (pid.equals(owners.get(resourceName))) {
            logger.logAccessRequest(pid, user, resourceName, "ALREADY_HELD",
                    "Process already holds this resource");
            return;
        }
        if (isWaiting(resourceName, pid)) {
            logger.logAccessRequest(pid, user, resourceName, "ALREADY_WAITING",
                    "Process is already in the FCFS waiting queue");
            return;
        }

        if (resourceManager.allocate(resourceName)) {
            owners.put(resourceName, pid);
            logger.logAccessRequest(pid, user, resourceName, "ALLOWED (ALLOCATED)",
                    "Resource lock acquired successfully");
        } else {
            queueFor(resourceName).add(process);
            logger.logAccessRequest(pid, user, resourceName, "QUEUED (FCFS)",
                    "Resource busy -> Placed in FCFS waiting queue");
        }
    }

    /* Releases a busy resource and hands the lock to the next process in the FCFS queue. */
    public void releaseAndServiceQueue(String resourceName) {
        Resource target = resourceManager.getResourceMap().get(resourceName);
        if (target == null) {
            System.out.println("[SYSTEM NOTICE] No such resource: " + resourceName);
            return;
        }
        if (target.isAvailable()) {
            System.out.println("[SYSTEM NOTICE] " + resourceName + " is not in use, nothing to release.");
            return;
        }

        String previousOwner = owners.remove(resourceName);
        resourceManager.release(resourceName);

        Queue<Process> queue = waitingQueues.get(resourceName);
        if (queue != null && !queue.isEmpty()) {
            Process next = queue.poll();
            resourceManager.allocate(resourceName);
            owners.put(resourceName, next.getProcessId());
            // Status text avoids the word "QUEUED": AuditLogger counts anything containing it as a queued request
            logger.logAccessRequest(next.getProcessId(), next.getOwner().getUsername(), resourceName,
                    "ALLOWED (FROM WAIT LIST)",
                    "Granted access from FCFS queue after Process " + previousOwner + " released lock");
        } else {
            System.out.println("[SYSTEM NOTICE] Queue empty. " + resourceName + " is now available.");
        }
    }

    /* Prints every resource's status, then who holds and who is waiting for the busy ones. */
    public void displayStatus() {
        resourceManager.display();
        for (String name : resourceManager.getResourceMap().keySet()) {
            String owner = owners.get(name);
            Queue<Process> queue = waitingQueues.get(name);
            if (owner == null && (queue == null || queue.isEmpty())) continue;

            StringBuilder waiting = new StringBuilder();
            if (queue != null) {
                for (Process p : queue) {
                    if (waiting.length() > 0) waiting.append(", ");
                    waiting.append(p.getProcessId());
                }
            }
            System.out.println(name + " -> held by " + (owner == null ? "none" : owner)
                    + " | waiting: " + (waiting.length() == 0 ? "none" : waiting.toString()));
        }
    }

    private Queue<Process> queueFor(String resourceName) {
        Queue<Process> queue = waitingQueues.get(resourceName);
        if (queue == null) {
            queue = new ArrayDeque<Process>();
            waitingQueues.put(resourceName, queue);
        }
        return queue;
    }

    private boolean isWaiting(String resourceName, String pid) {
        Queue<Process> queue = waitingQueues.get(resourceName);
        if (queue == null) return false;
        for (Process p : queue) {
            if (p.getProcessId().equals(pid)) return true;
        }
        return false;
    }
}
