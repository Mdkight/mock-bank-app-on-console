package com.revature.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.objects.User;
import com.revature.utils.ConnectionUtils;
import com.revature.utils.dao.UserDataPostgres;

public class BasicFunctions {
	static Scanner scan = new Scanner(System.in);
	static int latch;
	static UserDataPostgres userPost = new UserDataPostgres();
	static Connection conn;

	public static User hasAccount(String answer) throws SQLException {
		User currentUser = null;
		if (answer.equals("Y") || answer.equals("YES")) {
			currentUser = userLogin();

		} else if (answer.equals("N") || answer.equals("NO")) {
			System.out.println("Would you like one?");
			System.out.println("Y or N");
			currentUser = createUserAccount(scan.nextLine().toUpperCase());
			return currentUser;
		} else {
			currentUser = null;
		}
		return currentUser;
	}

	public static User userLogin() throws SQLException {
		User currentUser = null;
		latch = 0;

		while (latch == 0) {
			conn = ConnectionUtils.getInstance().getConnection();
			System.out.println("Please enter your username: ");
			String username = scan.nextLine();
			Statement getAllUsernames = conn.createStatement();
			ResultSet rs = getAllUsernames.executeQuery("Select * from users");
			while (rs.next()) {
				boolean isIt = username.equals(rs.getString("username"));
				if (isIt) {
					currentUser = userPost.getUser(username);
					System.out.println("Welcome back!");
					boolean correct = checkPassword(currentUser);
					if (correct) {
						return currentUser;
					}
				}
			}
		}

		return currentUser;

	}

	public static User createUserAccount(String answer) throws SQLException {
		User newUser = null;
		if (answer.equals("Y") || answer.equals("YES")) {
			String username;
			username = createUsername();
			newUser = createPassword(username);
			return newUser;

		}

		else if (answer.equals("N") || answer.equals("NO")) {
			System.out.println("If you don't have an account, and you dont want one, then I guess this is goodbye");
			System.exit(0);
		}

		return newUser;

	}

	public static boolean checkPassword(User currentUser) {
		boolean correctPass = false;
		latch = 0;
		while (latch == 0) {
			System.out.println("please enter your password");
			String password = scan.nextLine();
			if (password.equals(currentUser.getPassword())) {
				System.out.println("access granted");
				correctPass = true;
				return correctPass;
			} else {
				System.out.println("I'm sorry, that is not the correct password");
				latch = 0;
			}

		}
		return correctPass;
	}

	public static String createUsername() throws SQLException {
		String username = null;
		latch = 0;
		while (latch == 0) {
			conn = ConnectionUtils.getInstance().getConnection();
			System.out.println("please enter your desired username: ");
			username = scan.nextLine();
			Statement getAllUsernames = conn.createStatement();
			ResultSet rs = getAllUsernames.executeQuery("Select * from users");
			while (rs.next()) {
				boolean isIt = username.equals(rs.getString("username"));
				if (isIt) {
					System.out.println("I'm sorry, that username is not available");
				} else {
					return username;
				}
			}
		}
		return username;
	}

	public static User createPassword(String username) {
		User newUser = null;
		UserDataPostgres userPost = new UserDataPostgres();
		while (latch == 0) {
			System.out.println("please enter your desired password: ");
			String password = scan.nextLine();
			System.out.println("please confirm your password: ");
			String confirm = scan.nextLine();
			if (password.equals(confirm)) {
				System.out.println("thank you, we are now creating your user account");
				newUser = new User(username, password);
				userPost.createCustomer(username, password);
				return newUser;

			} else {
				System.out.println("those passwords do not match");
				latch = 0;
			}
		}
		return newUser;
	}

	public static int getIntInput() {
		int selection = 0;
		latch = 0;
		while (latch == 0) {
			String input = scan.nextLine();
			if (input.equals("b") || input.equals("B")) {
				selection = 0;
				return selection;
			} else if (input.equals("0")) {
				System.out.println("I'm sorry, 0 is not a valid entry");
			} else {
				try {
					selection = Integer.parseInt(input);
					return selection;
				} catch (NumberFormatException e) {
					System.out.println("that is not a valid integer entry");
					latch = 0;
				}
			}
		}
		return selection;
	}

	public static boolean isItThere(String query) {
		boolean isIt = false;
//		ResultSet rs = userPost.getAllUsernames();
		ResultSet rs = null;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
//			PreparedStatement getAllUsernames = conn.prepareStatement("Select username from users");
			Statement getAllUsernames = conn.createStatement();
			rs = getAllUsernames.executeQuery("Select * from users");
			while (rs.next()) {
				isIt = query.equals(rs.getString("username"));
				if (isIt) {
					return isIt;
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return isIt;
	}
}
