package com.revature.menus;

import java.util.Scanner;

import com.revature.objects.User;
import com.revature.utils.dao.AccountDataPostgres;
import com.revature.utils.dao.TransactionDataPostgres;
import com.revature.utils.dao.UserDataPostgres;

public class EmployeeMenu extends Menu {
	UserDataPostgres userPost = new UserDataPostgres();
	AccountDataPostgres acctPost = new AccountDataPostgres();
	TransactionDataPostgres transPost =new TransactionDataPostgres();
	Scanner scan = new Scanner(System.in);
	static int latch=0;
	
	public EmployeeMenu() {
		super("View pending accounts", "view accounts by customer", "see all transactions", "Exit");
	}

	@Override
	public void makeSelection(int entry, User currentUser) {

		switch(entry){
		case 1:
			acctPost.getAccount();
			acctPost.approveAccounts();
		case 2:
			acctPost.getAccount(userSelector());
		case 3:
			transPost.getTransactions();
		case 4:
			System.out.println("Goodbye");
			System.exit(0);
		default:
			
		}

		
	}
	}

