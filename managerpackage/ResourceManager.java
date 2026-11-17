package managerpackage;
//setup import statements

import java.util.LinkedHashMap;
import java.util.Map;
import resourcepackage.Resource;


public class ResourceManager {

    private Map<String, Resource> resourceMap;

    //default constructor
    public ResourceManager() {
        //hardcoded resources for testing purposes
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
        return resourceMap;
    }

    //adds a resource to the resource map
    //set to private because resources should only be added through the constructor, not by the user
    private void addResourceToMap(Resource r) {
        if (r==null || resourceMap.containsKey(r.getName())) {
            throw new IllegalArgumentException("Resource cannot be null");
        }
        if (resourceMap.containsKey(r.getName())) {
        throw new IllegalArgumentException("A resource named " + r.getName() + " already exists");
        }   
        resourceMap.put(r.getName(), r);
    }
    // public Resource getResource(Resource r) {
    //     return resourceMap.get(r.getName());
    // }

    public Resource getResource(String name) {
        Resource resource = resourceMap.get(name);
        if (resource == null) {
            System.out.println("\nThere is no such resource within system: " + name);
        }
        else {
            System.out.println("\nResource found: " + resource.getName());
            resource.display();
        }
        return resourceMap.get(name);
    }

    // public void displayResources() {
    //     System.out.println("\nResource Manager: Displaying all resources in the resource map");
    //     for (Resource r : resourceMap.values()) {
    //         r.display();
    //     }
    // }
    public void display() {
        System.out.printf("%-14s %-10s %s%n", "Name", "Type", "Status");
        System.out.println("-----------------------------------");
        for (Map.Entry<String, Resource> entry : resourceMap.entrySet()) {
            Resource r = entry.getValue();
            System.out.printf("%-14s %-10s %s%n",
                    entry.getKey(), r.getType(), r.isAvailable() ? "In Use" : "Available");
        }
    }
}
