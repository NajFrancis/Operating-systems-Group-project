package resourcepackage;

public class Resource {
    // Type names shared with the permission system
    public static final String TYPE_CPU = "CPU";
    public static final String TYPE_MEMORY = "Memory";
    public static final String TYPE_FILE = "File";
    public static final String TYPE_PROTECTED = "Protected";

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
        this.inUse = inUse;
        
    }
    //copy constructor 
    public Resource(Resource r){
        this.name = r.name;
        this.type = r.type;
        this.inUse = r.inUse;
    }
    //accessors and mutators 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean getInUse() {
        return inUse;
    }
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
    //Allocate Resource 
    public boolean allocate(){
        if(inUse){
            return false; //resource is already in use
        }
        inUse = true; //allocate resource
        return true;
    }
    //Release Resource Method
    public void release(){
        inUse = false; //release resource
    }
    //display method
    public void displayData() {
        System.out.println();
        System.out.println("Resource Name: " + name);
        System.out.println("Resource Type: " + type);
        System.out.println("status: " + inUse);
    }
    //custom Display method
    public void display(){
        System.out.println();
        System.out.println("resource name: " + name);
        System.out.println("resource type: " + type);
        System.out.println("status: " + (inUse ? "In Use" : "Available"));
    }


}
