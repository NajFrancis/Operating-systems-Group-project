import managerpackage.ResourceManager1;
import resourcepackage.Resource1;

public class app {
    public app() {
        // method
    }

    public static void main(String[] args) {
        ResourceManager1 resourceManager = new ResourceManager1();

        Resource1 cpuResource2 = resourceManager.getResource(new Resource1("CPU1", Resource1.TYPE_CPU));
        if (cpuResource2 == null) {
            System.out.println("\nResource not found: CPU1");
        }
        
    }
}