package com.revature.utils.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.main.BasicFunctions;
import com.revature.objects.Account;
import com.revature.objects.User;
import com.revature.utils.ConnectionUtils;

public class AccountDataPostgres {
	TransactionDataPostgres transPost = new TransactionDataPostgres();
	Connection conn;
	static int latch = 0;

	public void display(ResultSet res) {
		try {
			while (res.next()) {
				System.out.println();
				System.out.print("account num:" + res.getInt("account_id"));
				System.out.print(" Customer ID:" + res.getInt("owner_id"));
				System.out.print(" account balance:" + res.getInt("current_balance"));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void withdraw(Account act) {
		int withdrawAmount = checkWithdrawBalance(act);
		int newBalance = act.getCurrentBalance() - withdrawAmount;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement withdraw = conn
					.prepareStatement("update accounts set current_balance=? where account_id=?");
			withdraw.setInt(1, newBalance);
			withdraw.setInt(2, act.getAccountId());
			act.setCurrentBalance(newBalance);
			System.out
					.println("your new balance for Account: " + act.getAccountId() + " is " + act.getCurrentBalance());
			transPost.logTransaction("withdrawal", withdrawAmount, act.getAccountId(), act.getOwnerId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private int checkWithdrawBalance(Account act) {
		latch = 0;
		int withdraw = 0;
		int difference = 0;
		while (latch == 0) {
			System.out.println("how much would you like to withdraw?");
			withdraw = BasicFunctions.getIntInput();
			difference = act.getCurrentBalance() - withdraw;
			if (withdraw < 0 || difference < 0) {
				System.out.println("I'm sorry, that is not a valid entry");
			} else {
				return withdraw;
			}
		}
		return withdraw;
	}

	private int checkTransferBalance(Account act) {
		latch = 0;
		int transfer = 0;
		System.out.println("your current balance is: " + act.getCurrentBalance());
		while (latch == 0) {
			System.out.println("how much would you like to transfer?");
			transfer = BasicFunctions.getIntInput();
			int difference = act.getCurrentBalance() - transfer;
			if (difference < 0 || transfer < 0) {
				System.out.println("I'm sorry, that is not a valid amount");

			} else {
				return transfer;
			}
		}
		return transfer;
	}

	public void getAllAccounts(int ownId) {
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccount = conn.prepareStatement("Select * from accounts where owner_id=?");
			getAccount.setInt(1, ownId);
			ResultSet rs = getAccount.executeQuery();
			display(rs);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public Account getAccount(int actId) {
		Account currentAccount = new Account();

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccount = conn.prepareStatement("Select * from accounts where account_id=?");
			getAccount.setInt(1, actId);
			ResultSet rs = getAccount.executeQuery();
			while (rs.next()) {
				currentAccount.setAccountId(actId);
				currentAccount.setOwnerId(rs.getInt(2));
				currentAccount.setApproved(rs.getBoolean(3));
				currentAccount.setCurrentBalance(rs.getInt(4));
				return currentAccount;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return currentAccount;
	}

	public void getAccount() {

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			Statement getAccounts = conn.createStatement();
			ResultSet rs = getAccounts.executeQuery("select * from accounts where approved=false");
			display(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void deposit(Account act) {
		latch = 0;
		int sum = 0;
		int depositAmount = 0;
		while (latch == 0) {
			System.out.println("how much would you like to deposit?");
			depositAmount = BasicFunctions.getIntInput();
			if (depositAmount >= 0) {
				sum = act.getCurrentBalance() + depositAmount;
				latch = 1;
			} else {
				System.out.println("I'm sorry, you cannot deposit a negative amount");
			}
		}
		int newBalance = sum;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement deposit = conn
					.prepareStatement("update accounts set current_balance=? where account_id=?");
			conn.setAutoCommit(false);
			deposit.setInt(1, newBalance);
			deposit.setInt(2, act.getAccountId());
			deposit.execute();
			act.setCurrentBalance(newBalance);
			System.out
					.println("your new balance for Account: " + act.getAccountId() + " is " + act.getCurrentBalance());
			transPost.logTransaction("deposit", depositAmount, act.getAccountId(), act.getOwnerId());
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}

	}

	public void otherTransfer(User fromUser) {
		System.out.println("enter the account number you would like to transfer from: ");
		System.out.println("or press B to return to the previous screen");
		int fromAccount = BasicFunctions.getIntInput();
		if (fromAccount == 0) {
			System.out.println("whoops");
		} else {
			if (isItMyAccount(fromAccount, fromUser)) {
				Account transferFromAccount = getAccount(fromAccount);
				int transferAmount = checkTransferBalance(transferFromAccount);
				System.out.println("please enter the User Id number of the customer to whom you want to transfer: ");
				int destinationUser = BasicFunctions.getIntInput();

				Account destinationAccount = getFirstUserAccount(destinationUser);
				transfer(transferFromAccount, destinationAccount, transferAmount);

			}

		}

	}

	private void transfer(Account fromAccount, Account destinationAccount, int transferAmount) {
		int fromAccountNumber = fromAccount.getAccountId();
		int destinationAccountNumber = destinationAccount.getAccountId();
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement transferFrom = conn
					.prepareStatement("update accounts set current_balance=? where account_id=?");
			PreparedStatement createTransferFrom = conn.prepareStatement(
					"insert into transfers (origin_account_id, destination_account_id, transfer_type, amount) values (?,?,?,?)");

			transferFrom.setInt(1, fromAccount.getCurrentBalance() - transferAmount);
			transferFrom.setInt(2, fromAccount.getAccountId());
			createTransferFrom.setInt(1, fromAccountNumber);
			createTransferFrom.setInt(2, destinationAccountNumber);
//			createTransferFrom.setInt(3, fromAccount.getOwnerId());
			createTransferFrom.setString(3, "Transfer From");
			createTransferFrom.setInt(4, transferAmount);
			transferFrom.execute();
			createTransferFrom.execute();
			transPost.logTransaction("Transfer from", transferAmount, fromAccountNumber, fromAccount.getOwnerId());
			transPost.logTransaction("Transfer to", transferAmount, destinationAccountNumber,
					destinationAccount.getOwnerId());
		} catch (SQLException e) {
			System.out.println("transfer failed, please try again later");
			e.printStackTrace();
		}

	}

	public Account getFirstUserAccount(int intInput) {
		List<Account> allAccounts = new ArrayList<Account>();
		try {
			ResultSet rs;
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccount = conn.prepareStatement(
					"Select owner_id, account_id, current_balance from accounts where owner_id=?");
			getAccount.setInt(1, intInput);
			rs = getAccount.executeQuery();
			Account toAdd = new Account();
			while (rs.next()) {
				toAdd.setOwnerId(rs.getInt(1));				
				toAdd.setAccountId(rs.getInt(2));
				toAdd.setCurrentBalance(3);
				allAccounts.add(toAdd);
				return allAccounts.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allAccounts.get(0);

	}

	private boolean isItMyAccount(int account, User user) {
		boolean isMyAccount;

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccounts = conn.prepareStatement("select * from accounts where owner_id=?");
			getAccounts.setInt(1, user.getUserId());
			ResultSet rs = getAccounts.executeQuery();
			while (rs.next()) {
				if(account == rs.getInt("account_id")) {
				isMyAccount = true ;
				return isMyAccount;
				}else {
					System.out.println("that account Id does not match any of your accounts");

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void newAccount(User currentUser) {
		System.out.println("What would you like to deposit as the starting balance of your new account?");
		int initialDeposit = BasicFunctions.getIntInput();
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement newAccount = conn
					.prepareStatement("insert into accounts (owner_id, approved, current_balance) values (?,false,?)");
			newAccount.setInt(1, currentUser.getUserId());
			newAccount.setInt(2, initialDeposit);
			newAccount.execute();
			System.out.println("your new account is awaiting approval");
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void approveAccounts() {
		System.out.println("Please enter the account number you wish to approve");
		System.out.println("or press B to go back");
		int acctNum = BasicFunctions.getIntInput();
		if (acctNum == 0) {

		} else {
			try {
				conn = ConnectionUtils.getInstance().getConnection();
				PreparedStatement approveAccount = conn
						.prepareStatement("update accounts set approved=true where account_id=?");
				approveAccount.setInt(1, acctNum);
				approveAccount.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	public void declineAccounts() {
		System.out.println("Please enter the account number you wish to decline");
		System.out.println("or press B to go back");
		int acctNum = BasicFunctions.getIntInput();
		if (acctNum == 0) {

		} else {
			try {
				conn = ConnectionUtils.getInstance().getConnection();
				PreparedStatement declineAccount = conn
						.prepareStatement("update accounts set approved=false where account_id=?");
				declineAccount.setInt(1, acctNum);
				declineAccount.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	public void getMyAccounts(int userId) {
		ResultSet rs = null;

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccount = conn.prepareStatement(
					"Select account_id, owner_id, current_balance from accounts where owner_id=? and approved=true");
			getAccount.setInt(1, userId);
			rs = getAccount.executeQuery();
			display(rs);

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
