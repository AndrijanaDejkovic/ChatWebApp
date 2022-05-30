package def;

import java.util.ArrayList;

public class User {
	private String username;
	private String password;
	
	public static ArrayList<User> registeredUsers = new ArrayList<>();
	private static ArrayList<User> loggedUsers = new ArrayList<>();
	
	public static ArrayList<User> getLoggedUsers() {
		return loggedUsers;
	}

	public String getUsername() {
		return username;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		
		registeredUsers.add(this);
	}
	
	public static User logIn(String username1, String password1) {
		for (User user : registeredUsers) {
			if (user.username.equals(username1) && user.password.equals(password1)) {
				//loggedUsers.add(user);
				return user;
			}
		}
		
		return null;
	}
	
	public static void logUser(User user) {
		loggedUsers.add(user);
	}
	
	public static boolean isUsernameAvailable(String username1) {
		for (User user : registeredUsers) {
			if (user.username.equals(username1)) return false;
		}
		return true;
	}
	
}
