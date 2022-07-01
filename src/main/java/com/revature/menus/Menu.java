package com.revature.menus;

import java.util.Scanner;

import com.revature.objects.User;
import com.revature.utils.UserDataPostgres;

public abstract class Menu {
	private String[] menuItems;
	UserDataPostgres userPost = new UserDataPostgres();
	Scanner scan = new Scanner(System.in);
	static int latch = 0;

	public Menu(String... strings) {
		this.menuItems = strings;

	}

	public static void displayMenu(Menu menuItems) {
		int i = 1;
		for (String s : menuItems.getMenuItems()) {
			System.out.println(i + ". " + s);
			i++;
		}
		System.out.println("Please enter the number of your selection");
	}

	public String[] getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(String[] menuItems) {
		this.menuItems = menuItems;
	}

	public abstract void makeSelection(int entry, User currentUser);

	public int userSelector() {
		int userId = 0;
		latch = 0;
		while (latch == 0) {
			System.out.println("Please enter the user number associated with the customer");
			System.out.println("if you do not know it, enter 0");
			userId = scan.nextInt();
			if (userId == 0) {
				System.out.println("I'm sorry, you must have the user id of the customer to continue");
			}
		}

		return userId;
	}

}
