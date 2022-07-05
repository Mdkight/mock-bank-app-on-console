package com.revature.menus;

import com.revature.main.BasicFunctions;
import com.revature.objects.Account;
import com.revature.objects.User;
import com.revature.utils.dao.AccountDataPostgres;
import com.revature.utils.dao.TransactionDataPostgres;
import com.revature.utils.dao.UserDataPostgres;

public class CustomerMenu extends Menu {
	UserDataPostgres userPost = new UserDataPostgres();
	AccountDataPostgres acctPost = new AccountDataPostgres();
	TransactionDataPostgres transPost =new TransactionDataPostgres();

	public CustomerMenu() {
		super("Check account balances'", "see balance of single account", "Make a withdrawl", "Make a deposit", "Transfer to another customer", "Apply for a new account", "accept pending transfers", "See my transactions", "Exit");
	}

	@Override
	public void makeSelection(int entry, User currentUser) {
		
		switch(entry){
		case 1:
			acctPost.getMyAccounts(currentUser.getUserId());
			break;
		case 2:
			System.out.println("Which account balance would you like to view?");
			Account currentAccount = acctPost.getAccount(BasicFunctions.getIntInput());
			currentAccount.toString();
			break;
		case 3:
			System.out.println("From which account would you like to withdraw?");
			acctPost.withdraw(acctPost.getAccount(BasicFunctions.getIntInput()));
			break;
		case 4:
			System.out.println("Into which account are you depositing?");
			acctPost.deposit(acctPost.getAccount(BasicFunctions.getIntInput()));
			break;
		case 5:
			acctPost.otherTransfer(currentUser);
			break;
		case 6:
			acctPost.newAccount(currentUser);
			break;
		case 7:
			transPost.seePendingTransfers(currentUser);
			transPost.selectPending();
			break;
		case 8:
			transPost.getTransactions(currentUser.getUserId());
			break;
		case 9:	
			System.out.println("Goodbye");
			System.exit(0);
		default:
			break;
		}
		
	}
	
}
