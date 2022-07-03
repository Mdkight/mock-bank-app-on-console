package com.revature.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.objects.User;
import com.revature.utils.dao.UserDataPostgres;

public class BasicFunctions {
	static Scanner scan = new Scanner(System.in);
	static int latch;
	static UserDataPostgres userPost = new UserDataPostgres();

	public static User hasAccount(String answer) {
		User currentUser = null;
		if (answer.equals("Y") || answer.equals("YES")) {
			currentUser = userLogin();

		} else if (answer.equals("N") || answer.equals("NO")) {
			System.out.println("Would you like one?");
			System.out.println("Y or N");
			currentUser = createUserAccount(scan.nextLine().toUpperCase());
		} else {
			currentUser = null;
		}
		return currentUser;
	}

	public static User userLogin() {
		User currentUser = null;
		latch = 0;
		while (latch == 0) {
			System.out.println("Please enter your username: ");
			String username = scan.nextLine();
			boolean isIt = isItThere(username, userPost.getAllUsernames());
			if (isIt) {
				currentUser = userPost.getUser(username);
				System.out.println("Welcome back!");
				checkPassword(currentUser);
				latch = 1;
			} else {
				System.out.println("That username does not exist");
				latch = 0;
			}
		}
		return currentUser;

	}

	public static User createUserAccount(String answer) {
		User newUser = null;
		if (answer.equals("Y") || answer.equals("YES")) {
			String username = createUsername();
			newUser = createPassword(username);
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
				latch = 1;
			} else {
				System.out.println("I'm sorry, that is not the correct password");
				latch = 0;
			}

		}
		return correctPass;
	}

	public static String createUsername() {
		UserDataPostgres userPost = new UserDataPostgres();
		String username = null;
		latch = 0;
		while (latch == 0) {
			System.out.println("please enter your desired username: ");
			username = scan.nextLine();
			boolean isIt = isItThere(username, userPost.getAllUsernames());
			if (isIt) {
				System.out.println("That username is not available");
				latch = 0;
			} else {
				latch = 1;
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
				latch = 1;

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
			} else if (input.equals("0")) {
				System.out.println("I'm sorry, 0 is not a valid entry");
			}else {
				try {
				selection =Integer.parseInt(input);
				latch=1;
				}catch (NumberFormatException e) {
					System.out.println("that is not a valid integer entry");
					latch =0;
				}
			}
		}
		return selection;
	}

	public static boolean isItThere(String query, ResultSet rs) {
		boolean isIt = false;
		try {
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
