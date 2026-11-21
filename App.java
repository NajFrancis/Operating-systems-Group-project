/*Amoya Collins 2408831*/
package member3;

import managerpackage.ResourceManager;
import resourcepackage.Resource;

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
        
        User admin = new User("Saiki", User.ADMIN);
        User user = new User("Amy", User.USER);
        //Creates new user accounts
        
        Resource registry=manager.getResource("Registry");
        Resource cpu = manager.getResource("CPU0");
        //Retrieves the named resource
        
        AccessController access = new AccessController();
        access.requestAccess(admin, registry);
        access.requestAccess(user, registry);
        access.requestAccess(user, cpu);
        //requests access to the resource with a role
    }
}



   