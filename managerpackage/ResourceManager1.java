package managerpackage;
//setup import statements

import java.util.LinkedHashMap;
import java.util.Map;
import resourcepackage.Resource1;


public class ResourceManager1 {

    private Map<String, Resource1> resourceMap;

    //default constructor
    public ResourceManager1() {
        //hardcoded resources for testing purposes
        resourceMap = new LinkedHashMap<>();
        addResourceToMap(new Resource1("CPU0",         Resource1.TYPE_CPU));
        addResourceToMap(new Resource1("RAM_Block1",   Resource1.TYPE_MEMORY));
        addResourceToMap(new Resource1("FileA.txt",    Resource1.TYPE_FILE));
        addResourceToMap(new Resource1("FileB.txt",    Resource1.TYPE_FILE));
        addResourceToMap(new Resource1("Registry",     Resource1.TYPE_PROTECTED));
        addResourceToMap(new Resource1("KernelConfig", Resource1.TYPE_PROTECTED));
    }

    //accessors and mutators
    public Map<String, Resource1> getResourceMap() {
        return resourceMap;
    }

    //adds a resource to the resource map
    public void addResourceToMap(Resource1 r) {
        resourceMap.put(r.getName(), r);
    }
    // public Resource getResource(Resource r) {
    //     return resourceMap.get(r.getName());
    // }
    public Resource1 getResource(String name) {
    return resourceMap.get(name);
    }

}
