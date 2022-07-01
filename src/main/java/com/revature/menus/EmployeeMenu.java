package com.revature.menus;

import java.util.Scanner;

import com.revature.objects.User;
import com.revature.utils.UserDataPostgres;

public class EmployeeMenu extends Menu {
	UserDataPostgres userPost = new UserDataPostgres();
	Scanner scan = new Scanner(System.in);
	static int latch=0;
	
	public EmployeeMenu() {
		super("View pending accounts", "view accounts by customer", "view customers name and Id", "see all transactions", "Exit");
	}

	@Override
	public void makeSelection(int entry, User currentUser) {

		switch(entry){
		case 1:
			userPost.getAccounts(false);
			
		case 2:
			int user=userSelector();
			if(user==0) {
				latch=0;
			}else {
			userPost.getAccounts(user);
			}
		case 3:
			
		case 4:
			
		case 5:
			System.out.println("Goodbye");
			System.exit(0);
		default:
			
		}

		
	}
	}

