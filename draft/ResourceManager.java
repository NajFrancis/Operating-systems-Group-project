package draft;
//setup inport statements

import java.util.LinkedHashMap;
import java.util.Map;


public class ResourceManager {

    private Map<String, Resource> resourceMap;

    //default constructor
    public ResourceManager() {
        resourceMap = new LinkedHashMap<>();
        addResource(new Resource("CPU0",         Resource.TYPE_CPU));
        addResource(new Resource("RAM_Block1",   Resource.TYPE_MEMORY));
        addResource(new Resource("FileA.txt",    Resource.TYPE_FILE));
        addResource(new Resource("FileB.txt",    Resource.TYPE_FILE));
        addResource(new Resource("Registry",     Resource.TYPE_PROTECTED));
        addResource(new Resource("KernelConfig", Resource.TYPE_PROTECTED));
    }

    //accessors and mutators

    //adds a resource to the resource map
    public void addResource(Resource r) {
        resourceMap.put(r.getName(), r);
    }
    public Resource getResource(Resource r) {
        return resourceMap.get(r.getName());
        
    }

}
