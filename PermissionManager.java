/*Amoya Collins 2408831*/
package member3;


public class PermissionManager {
	public boolean checkPermission(String role, String resourceType) {
		switch (role) {
			case "Administrator":
				return true;
			case "User":
				return !resourceType.equals("Protected");
			default:
				return false;
		}//Case statement to regulate permissions
	}
}
