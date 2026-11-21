//Najaun Francis - 2304842
//resource creation and allocation 

package managerpackage;
//setup import statements

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import resourcepackage.Resource;


public class ResourceManager {
    private static final boolean DEBUG = true; //true to show debug false to hide them
    private final Map<String, Resource> resourceMap;

    //default constructor
    public ResourceManager() {
        //fixed resource setup, done once at start of program, 
        // no new resources can be added or removed during runtime
        resourceMap = new LinkedHashMap<>();
        addResourceToMap(new Resource("CPU0",        Resource.TYPE_CPU));
        addResourceToMap(new Resource("RAM_Block1",   Resource.TYPE_MEMORY));
        addResourceToMap(new Resource("FileA.txt",    Resource.TYPE_FILE));
        addResourceToMap(new Resource("FileB.txt",    Resource.TYPE_FILE));
        addResourceToMap(new Resource("Registry",     Resource.TYPE_PROTECTED));
        addResourceToMap(new Resource("KernelConfig", Resource.TYPE_PROTECTED));
    }

    //accessors and mutators

    //returns the resource map
    public Map<String, Resource> getResourceMap() {
        // return resourceMap;
        //returns the resource map which is a security risk so instead,
        // return an unmodifiable view of the resource map to prevent external modification
        return Collections.unmodifiableMap(resourceMap);
    }

    //adds a resource to the resource map
    //set to private because resources should only be added through the constructor, not by the user
    private void addResourceToMap(Resource r) {
        if (r==null) {
            throw new IllegalArgumentException("Resource cannot be null");
        }
        if (resourceMap.containsKey(r.getName())) {
        throw new IllegalArgumentException("A resource named " + r.getName() + " already exists");
        }   
        resourceMap.put(r.getName(), r);
    }

    public Resource getResource(String name) {
        Resource resource = resourceMap.get(name);
        if (DEBUG){
            if (resource == null) {
                System.out.println("\nThere is no such resource within system: " + name);
            }
            else {
                System.out.println("\nResource found: " + resource.getName());
                resource.display();
            }
        }
        // return resourceMap.get(name);
        return resource;
    }
    //manager method to allocate a resource by name, 
    // returns true if allocation was successful, false if it was not
    public boolean allocate(String name) {
        Resource resource = resourceMap.get(name);
        if (resource == null) {
            if (DEBUG) {
                System.out.println("\nThere is no such resource within system: " + name);
            }
            return false;
        }
        return resource.allocate();
    }

    //Returns true only when an allocated resource was successfully released.
    public boolean release(String name) {
        Resource resource = resourceMap.get(name);
        if (resource == null) {
            if (DEBUG) {
                System.out.println("\nThere is no such resource within system: " + name);
            }
            return false;
        }
        return resource.release();
    }
    public boolean isAvailable(String name) {
        //looks up the resource by name
        Resource resource = resourceMap.get(name);
        if (resource == null) {
            if (DEBUG) {
                System.out.println("There is no such resource within system: " + name);
            }
            //not found or in use
            return false;
        }

        boolean available = resource.isAvailable();
        if (DEBUG) {
            System.out.println("Resource " + name + " is " + (available ? "available" : "in use"));
        }

        return available;
    }

    public void display() {
        System.out.printf("%-14s %-10s %s%n", "Name", "Type", "Status");
        System.out.println("-----------------------------------");
        for (Map.Entry<String, Resource> entry : resourceMap.entrySet()) {
            Resource r = entry.getValue();
            System.out.printf("%-14s %-10s %s%n",
                    entry.getKey(), r.getType(), r.isAvailable() ? "Available" : "In Use");
        }
        System.out.println("-----------------------------------");
    }
}
