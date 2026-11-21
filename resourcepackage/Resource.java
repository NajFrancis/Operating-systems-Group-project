//Najaun Francis - 2304842
//resource creation and allocation 
package resourcepackage;


public class Resource {
    private static final boolean DEBUG = true;//true to show debug false to hide them

    // Constants shared with the permission system
    public static final String TYPE_CPU = "CPU";
    public static final String TYPE_MEMORY = "Memory";
    public static final String TYPE_FILE = "File";
    public static final String TYPE_PROTECTED = "Protected";

    //attributes 
    private String name;
    private String type;
    private boolean inUse;
    
    //Default constructor 
    public Resource() {
        name = "Default Resource";
        type = "Default Type";
        inUse = false;
    }
    //primary constructor 
    public Resource(String name, String type) {
        this.name = name;
        this.type = type;
        this.inUse = false;
    }
    //accessors and mutators 
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isAvailable() {
        return !inUse;
    }
    //This method allocates the resource if it is available, and returns true if the allocation was successful, false if it was not
    public boolean allocate() {
        if (!isAvailable()) {

            //resource is already in use, cannot allocate
            if (DEBUG){
            System.out.println("\nResource " + name + " is already in use, cannot allocate");
                }
            return false;
        }
        //resource is not in use, can allocate
        inUse = true;
        //debug message to show that the resource has been allocated
        if (DEBUG){
            System.out.println("\nResource " + name + " has been allocated and is now in use");
        }
        return true;
    }

    //Returns true only when an allocated resource was successfully released.
    public boolean release(){
        if (!inUse) {
            if (DEBUG) {
                System.out.println("\nResource " + name + " is not in use, cannot release");
            }
            return false;
        }
        inUse = false;
        if (DEBUG) {
            System.out.println("\nResource " + name + " has been released and is now available");
        }
        return true;
    }
    //custom Display method
    public void display(){
        System.out.println("resource name: " + name);
        System.out.println("resource type: " + type);
        System.out.println("status: " + (inUse ? "In Use" : "Available"));
        System.out.println("-----------------------------");
    }
}
