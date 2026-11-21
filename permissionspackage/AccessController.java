/*Amoya Collins 2408831*/

package permissionspackage;

import resourcepackage.Resource;

public class AccessController {
	private PermissionManager permissionManager;
	
	public AccessController() {
		//object of permission manager
		permissionManager = new PermissionManager();
	}
	public boolean requestAccess(User user, Resource resource) {
		//checks if user has permission and grants access
		boolean allowed= permissionManager.checkPermission(user.getRole(), resource.getType());
		if (allowed) {
			System.out.println("ACCESS GRANTED :)");
		} else {
			System.out.println("ACCESS DENIED :(");
		}
		return allowed;
	}
}
