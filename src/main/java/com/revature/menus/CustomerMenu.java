package com.revature.menus;

import com.revature.main.BasicFunctions;
import com.revature.objects.User;
import com.revature.utils.dao.AccountDataPostgres;
import com.revature.utils.dao.TransactionDataPostgres;
import com.revature.utils.dao.UserDataPostgres;

public class CustomerMenu extends Menu {
	UserDataPostgres userPost = new UserDataPostgres();
	AccountDataPostgres acctPost = new AccountDataPostgres();
	TransactionDataPostgres transPost =new TransactionDataPostgres();

	public CustomerMenu() {
		super("Check account balances'", "see balance of single account", "Make a withdrawl", "Make a deposit", "Transfer to another customer", "Apply for a new account", "See my transactions", "Exit");
	}

	@Override
	public void makeSelection(int entry, User currentUser) {
		
		switch(entry){
		case 1:
			acctPost.display(acctPost.getMyAccounts(currentUser.getUserId()));
		case 2:
			System.out.println("Which account balance would you like to view?");
			acctPost.getAccount(BasicFunctions.getIntInput());
		case 3:
			System.out.println("From which account would you like to withdraw?");
			acctPost.withdraw(acctPost.getAccount(BasicFunctions.getIntInput()));
		case 4:
			System.out.println("Into which account are you depositing?");
			acctPost.deposit(acctPost.getAccount(BasicFunctions.getIntInput()));
		case 5:
			acctPost.otherTransfer(currentUser);
		case 6:
			acctPost.newAccount(currentUser);
		case 7:
			transPost.display(transPost.getTransactions(currentUser.getUserId()));
		case 8:	
			System.out.println("Goodbye");
			System.exit(0);
		}
		
	}
	
}
