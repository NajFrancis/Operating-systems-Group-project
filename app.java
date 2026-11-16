import managerpackage.ResourceManager1;
import resourcepackage.Resource;

public class app {
    public static void main(String[] args) {
        ResourceManager1 resourceManager = new ResourceManager1();

        Resource cpuResource2 = resourceManager.getResource("CPU0");
        if (cpuResource2 == null) {
            System.out.println("\nResource not found: CPU0");
        }
        
    } 

}