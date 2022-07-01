package com.revature.menus;

import com.revature.objects.User;
import com.revature.utils.AccountDataPostgres;
import com.revature.utils.UserDataPostgres;

public class CustomerMenu extends Menu {
	UserDataPostgres userPost = new UserDataPostgres();
	AccountDataPostgres acctPost = new AccountDataPostgres();

	public CustomerMenu() {
		super("Check account balances'", "Make a withdrawl", "Make a deposit", "Transfer between your accounts", "Transfer to another customer", "Apply for a new account","See my transactions", "Exit");
	}

	@Override
	public void makeSelection(int entry, User currentUser) {
		
		switch(entry){
		case 1:
			userPost.getAccounts(currentUser.getUserId());
		case 2:
			acctPost.withdraw();
		case 3:
			acctPost.deposit();
		case 4:
			acctPost.selfTransfer();
		case 5:
			acctPost.otherTransfer();
		case 6:
			acctPost.newAccount();
		case 7:
			
		case 8:	
			System.out.println("Goodbye");
			System.exit(0);
		}
		
	}
	
}
