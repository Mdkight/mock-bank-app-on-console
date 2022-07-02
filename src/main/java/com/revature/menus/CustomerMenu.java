package com.revature.menus;

import com.revature.main.BasicFunctions;
import com.revature.objects.User;
import com.revature.utils.dao.AccountDataPostgres;
import com.revature.utils.dao.UserDataPostgres;

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
			System.out.println("From which account would you like to withdraw?");
			acctPost.withdraw(acctPost.getAccount(BasicFunctions.getIntInput()));
		case 3:
			System.out.println("Into which account are you depositing?");
			acctPost.deposit(acctPost.getAccount(BasicFunctions.getIntInput()));
		case 4:
			acctPost.selfTransfer();
		case 5:
			acctPost.otherTransfer();
		case 6:
			acctPost.newAccount(currentUser);
		case 7:
			userPost.display(userPost.getTransactions(currentUser.getUserId()));
		case 8:	
			System.out.println("Goodbye");
			System.exit(0);
		}
		
	}
	
}
