/*Amoya Collins 2408831*/
package member3;

public class User {
	private String username;
	private String role;
	
	//Creates constants. These are the only roles involved in the process
	public static final String ADMIN = "Administrator";
	public static final String USER = "User";
	
	//Parameterized constructor
	public User(String username, String role) {
		this.username = username;
		this.role = role;
	}
	
	//Getters
	public String getUsername() {
		return username;
	}
	
	public String getRole() {
		return role;
	}
}
