package simulationpackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loggingpackage.AuditLogger;
import managerpackage.ResourceManager;
import member4.Process;
import member4.QNode;
import member4.Queue;
import permissionspackage.AccessController;
import permissionspackage.User;
import resourcepackage.Resource;

/*
 * Access pipeline: Request -> Permission Check -> Allocation / Queueing -> Security Logging.
 *
 *  - permissions:  AccessController (permissionspackage), checked against the requesting user
 *  - locks:        ResourceManager.allocate / release
 *  - queues:       member4.Queue (Enqueue = waiting queue, EnqueueRQueue = ready queue,
 *                  processWaitingQueue = FCFS servicing)
 *  - logging:      AuditLogger
 *
 * member4.Process only stores an id and a resource name, so the owner of each process is tracked here.
 */
public class AccessPipeline {

    private static class Info {
        final int id;
        final User owner;

        Info(int id, User owner) {
            this.id = id;
            this.owner = owner;
        }
    }

    private final ResourceManager resourceManager;
    private final AccessController accessController;
    private final AuditLogger logger;

    private final Queue queue = new Queue();
    private final Map<Process, Info> info = new HashMap<Process, Info>();
    private final Map<String, Integer> holders = new HashMap<String, Integer>(); // resource -> pid holding it
    private final List<Process> readyList = new ArrayList<Process>();            // display copy: the ready queue has no getters

    public AccessPipeline(ResourceManager resourceManager, AccessController accessController, AuditLogger logger) {
        this.resourceManager = resourceManager;
        this.accessController = accessController;
        this.logger = logger;
    }

    /* A user asks for a resource: a process is created and sent through the pipeline. */
    public void submitRequest(User owner, String resourceName) {
        String user = owner.getUsername();

        Process p = new Process();
        int id = p.generateProcessID();
        p.setresourceRequired(resourceName);
        info.put(p, new Info(id, owner));
        System.out.println("Process starting with ID " + id + "\n Requesting access to " + resourceName);

        // getResourceMap().get() avoids the debug printing that ResourceManager.getResource() does
        Resource target = resourceManager.getResourceMap().get(resourceName);
        if (target == null) {
            logger.logAccessRequest(String.valueOf(id), user, resourceName, "NOT_FOUND",
                    "Resource missing from system directory");
            return;
        }

        if (!accessController.requestAccess(owner, target)) {
            logger.logAccessRequest(String.valueOf(id), user, resourceName, "DENIED (SECURITY VIOLATION)",
                    "Access denied by permission policy");
            return;
        }

        if (resourceManager.allocate(resourceName)) {
            holders.put(resourceName, id);
            queue.EnqueueRQueue(p);
            readyList.add(p);
            logger.logAccessRequest(String.valueOf(id), user, resourceName, "ALLOWED (ALLOCATED)",
                    "Resource lock acquired successfully");
        } else {
            queue.Enqueue(p);
            logger.logAccessRequest(String.valueOf(id), user, resourceName, "QUEUED (FCFS)",
                    "Resource busy -> Placed in FCFS waiting queue");
        }
    }

    /* Releases a busy resource, then lets the waiting queue be served in FCFS order. */
    public void releaseAndService(String resourceName) {
        Resource target = resourceManager.getResourceMap().get(resourceName);
        if (target == null) {
            System.out.println("[SYSTEM NOTICE] No such resource: " + resourceName);
            return;
        }
        if (target.isAvailable()) {
            System.out.println("[SYSTEM NOTICE] " + resourceName + " is not in use, nothing to release.");
            return;
        }

        Integer holder = holders.remove(resourceName);
        resourceManager.release(resourceName);
        System.out.println("[SYSTEM NOTICE] " + resourceName + " released"
                + (holder == null ? "" : " by process " + holder) + ".");

        serviceWaitingQueue();
    }

    /*
     * One FCFS pass over the waiting queue. Queue.processWaitingQueue takes the front process,
     * allocates its resource and releases it again (the process runs to completion), or puts it back
     * at the rear if the resource is still busy. Each waiting process is looked at once per pass.
     */
    private void serviceWaitingQueue() {
        int waiting = countWaiting();
        if (waiting == 0) {
            System.out.println("[SYSTEM NOTICE] Waiting queue is empty.");
            return;
        }
        for (int i = 0; i < waiting; i++) {
            Process candidate = queue.getFront().getData();
            queue.processWaitingQueue(resourceManager);
            if (isWaiting(candidate)) continue; // resource still busy: it went back to the rear

            Info entry = info.get(candidate);
            queue.EnqueueRQueue(candidate);
            readyList.add(candidate);
            // Status text avoids the word "QUEUED": AuditLogger counts anything containing it as a queued request
            logger.logAccessRequest(String.valueOf(entry.id), entry.owner.getUsername(),
                    candidate.getresourceRequired(), "ALLOWED (FROM WAIT LIST)",
                    "Served FCFS from waiting queue: resource allocated, then released on completion");
        }
    }

    public void displayStatus() {
        resourceManager.display();
        for (String name : resourceManager.getResourceMap().keySet()) {
            Integer holder = holders.get(name);
            if (holder != null) System.out.println(name + " -> held by process " + holder);
        }
    }

    public void displayQueues() {
        System.out.println("Waiting queue (FCFS):");
        int n = 0;
        for (QNode node = queue.getFront(); node != null; node = node.getnextNode()) {
            n++;
            System.out.println("  " + n + ". " + describe(node.getData()));
        }
        if (n == 0) System.out.println("  (empty)");

        System.out.println("Ready queue:");
        if (readyList.isEmpty()) System.out.println("  (empty)");
        for (int i = 0; i < readyList.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + describe(readyList.get(i)));
        }
    }

    private int countWaiting() {
        int n = 0;
        for (QNode node = queue.getFront(); node != null; node = node.getnextNode()) n++;
        return n;
    }

    private boolean isWaiting(Process p) {
        for (QNode node = queue.getFront(); node != null; node = node.getnextNode()) {
            if (node.getData() == p) return true;
        }
        return false;
    }

    private String describe(Process p) {
        Info entry = info.get(p);
        String id = (entry == null) ? "?" : String.valueOf(entry.id);
        String owner = (entry == null) ? "?" : entry.owner.getUsername();
        return "PID " + id + " (" + owner + ") -> " + p.getresourceRequired();
    }
}
