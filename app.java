import managerpackage.ResourceManager;

public class App {
    public static void main(String[] args) {
        //create a new resource manager object
        ResourceManager manager = new ResourceManager();
        //displays the whole resource list
        manager.display();
        //allocates a resouse 
        manager.getResource("Registry").allocate();
        // manager.getResource("Registry").release();
        manager.display();
        manager.isAvailable("Registry");



    } 

}