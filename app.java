import managerpackage.ResourceManager;

public class App {
    public static void main(String[] args) {
        //create a new resource manager object
        ResourceManager manager = new ResourceManager();
        //displays the whole resource list
        manager.display();
        //allocates a resource
        manager.allocate("Registry");
        manager.isAvailable("Registry");
        //checks if resource is available
        // manager.isAvailable("Registry");
        // //releases a resource from use
        // manager.release("Registry");
        // //displays entire hashmap/resource list
        manager.display();



    } 
}