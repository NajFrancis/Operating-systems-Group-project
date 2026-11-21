package userpackage;

import permissionspackage.User;

/*
 * STAND-IN for the missing member's Process class.
 * Written to match exactly how App.java used it:
 *   new Process(id, owner, flag), getProcessId(), getOwner().
 * Replace with the real class once it exists.
 */
public class Process {
    private String processId;
    private User owner;
    private boolean sandboxed; // GUESS: App.java passes a boolean as the 3rd argument; meaning unconfirmed

    public Process(String processId, User owner, boolean sandboxed) {
        this.processId = processId;
        this.owner = owner;
        this.sandboxed = sandboxed;
    }

    public String getProcessId() {
        return processId;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isSandboxed() {
        return sandboxed;
    }

    public void display() {
        System.out.println("Process " + processId + " | owner: " + owner.getUsername()
                + " (" + owner.getRole() + ")");
    }
}
