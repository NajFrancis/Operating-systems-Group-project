package resourcepackage;

public class Resource {
    // Type names shared with the permission system
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
            //debug message to show that the resource is already in use
            System.out.println("\nResource " + name + " is already in use, cannot allocate");
            return false;
        }
        //resource is not in use, can allocate
        inUse = true;
        //debug message to show that the resource has been allocated
        System.out.println("\nResource " + name + " has been allocated and is now in use");
        return true;
    }

    //This makes the resource available to be used 
    public void release(){
        inUse = false; //release resource
        //debug message to show that the resource has been released
        System.out.println("\nResource " + name + " has been released and is now available");
    }
    
    //custom Display method
    public void display(){
        System.out.println("resource name: " + name);
        System.out.println("resource type: " + type);
        System.out.println("status: " + (inUse ? "In Use" : "Available"));
        System.out.println("-----------------------------");
    }


}
