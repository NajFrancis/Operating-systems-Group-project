import managerpackage.ResourceManager;
import resourcepackage.Resource;

public class App {
    public static void main(String[] args) {
        //create a new resource manager object
        ResourceManager manager = new ResourceManager();
        //creates a new resource object then calls the getResource method to find the resource in the resource map
        //NOTE resource names are TYPE SENSITIVE, so the name must match exactly or it won't be found
        Resource cpu = manager.getResource("CPU0");

        //try to allocate the resource 
        cpu.allocate();
        manager.getResource("CPU0");
        //try to allocate the resource again, should fail since it is already in use
        // cpu.allocate();
        // Manager.getResource("CPU0");

        //release the resource and try to allocate it again, should succeed since it is now available
        // cpu.release();
        // Manager.getResource("CPU0");


        Resource ram = manager.getResource("RAM_Block1");
        ram.allocate();
        manager.getResource("RAM_Block1");
        ram.release();
        manager.getResource("RAM_Block1");

        manager.displayResources();

    } 

}