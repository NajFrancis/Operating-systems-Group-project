package resourcepackage;

public class Resource1 {
    // Type names shared with the permission system
    public static final String TYPE_CPU = "CPU";
    public static final String TYPE_MEMORY = "Memory";
    public static final String TYPE_FILE = "File";
    public static final String TYPE_PROTECTED = "Protected";

    private String name;
    private String type;
    private boolean inUse;

    //Default constructor 
    public Resource1() {
        name = "Default Resource";
        type = "Default Type";
        inUse = false;
    }
    //primary constructor 
    public Resource1(String name, String type) {
        this.name = name;
        this.type = type;
        this.inUse = false;
    }
    //copy constructor 
    public Resource1(Resource1 r){
        this.name = r.name;
        this.type = r.type;
        this.inUse = r.inUse;
    }
    //accessors and mutators 
    public String getName() {
        return name;
    }
    //commented out because of security risk, REMOVING LATER (breaks the map, if a name is changed, 
    // the map will not be able to find the resource)
    // public void setName(String name) {
    //     this.name = name;
    // }
    public String getType() {
        return type;
    }
    //commented out because of security risk, REMOVING LATER
    // public void setType(String type) {
    //     this.type = type;
    // }
    // public boolean getInUse() {
    //     return inUse;
    // }

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
    //custom Display method
    public void display(){
        System.out.println();
        System.out.println("resource name: " + name);
        System.out.println("resource type: " + type);
        System.out.println("status: " + (inUse ? "In Use" : "Available"));
    }


}
