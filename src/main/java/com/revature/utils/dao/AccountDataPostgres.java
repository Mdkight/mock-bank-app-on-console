package com.revature.utils.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.revature.main.BasicFunctions;
import com.revature.objects.Account;
import com.revature.objects.User;
import com.revature.utils.ConnectionUtils;

public class AccountDataPostgres {

	Connection conn;
	static int latch = 0;

	public void display(ResultSet res) {
		try {
			while (res.next()) {
				System.out.println(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void withdraw(Account act) {
		int newBalance = checkWithdrawBalance(act);
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement withdraw = conn
					.prepareStatement("update accounts set current_balance=? where account_id=?");
			withdraw.setInt(1, newBalance);
			withdraw.setInt(2, act.getAccountId());
			act.setCurrentBalance(newBalance);
			System.out
					.println("your new balance for Account: " + act.getAccountId() + " is " + act.getCurrentBalance());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private int checkWithdrawBalance(Account act) {
		latch = 0;
		int withdraw=0;
		int difference =0;
		while (latch == 0) {
			System.out.println("how much would you like to withdraw?");
			withdraw = BasicFunctions.getIntInput();
			difference = act.getCurrentBalance() - withdraw;			
			if (withdraw < 0 || difference<0) {
				System.out.println("I'm sorry, that is not a valid entry");
			} else {
				latch = 1;
			}
		}
		return difference;
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
				latch = 1;
			}
		}
		return transfer;
	}

	public Account getAccount(int actId) {
		Account currentAccount = new Account();

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccount = conn.prepareStatement("Select * from accounts where account_id=?");
			getAccount.setInt(1, actId);
			ResultSet rs = getAccount.executeQuery();
			currentAccount.setAccountId(actId);
			currentAccount.setOwnerId(rs.getInt(2));
			currentAccount.setApproved(rs.getBoolean(3));
			currentAccount.setCurrentBalance(rs.getInt(4));
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return currentAccount;
	}

	public void getAccount() {

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			Statement getAccounts = conn.createStatement();
			ResultSet rs = getAccounts.executeQuery("select * from accounts where approved is null");
			display(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void deposit(Account act) {
		latch = 0;
		int sum = 0;
		while (latch == 0) {
			System.out.println("how much would you like to deposit?");
			int deposit = BasicFunctions.getIntInput();
			if (deposit >= 0) {
				sum = act.getCurrentBalance() + deposit;
				latch = 1;
			} else {
				System.out.println("I'm sorry, you cannot deposit a negative amount");
			}
		}
		int newBalance = sum;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement withdraw = conn
					.prepareStatement("update accounts set current_balance=? where account_id=?");
			withdraw.setInt(1, newBalance);
			withdraw.setInt(2, act.getAccountId());
			act.setCurrentBalance(newBalance);
			System.out
					.println("your new balance for Account: " + act.getAccountId() + " is " + act.getCurrentBalance());
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}

	}

	public void otherTransfer(User fromUser) {
		Account toUser = new Account();
		System.out.println("enter the account number you would like to transfer from: ");
		System.out.println("or press B to return to the previous screen");
		int fromAccount = BasicFunctions.getIntInput();
		if (fromAccount == 0) {

		} else {
			if (isItMyAccount(fromAccount, fromUser)) {

				int transferAmount = checkTransferBalance(getAccount(BasicFunctions.getIntInput()));
				System.out.println("please enter the User Id number of the customer to whom you want to transfer: ");
				int destinationAccount = getFirstUserAccount(BasicFunctions.getIntInput());
				transfer(fromAccount, destinationAccount, transferAmount);

			}

		}

	}

	private void transfer(int fromAccount, int destinationAccount, int transferAmount) {
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			CallableStatement transfer = conn.prepareCall("transfer(?,?,?)");
			transfer.setInt(1, fromAccount);
			transfer.setInt(2, destinationAccount);
			transfer.setInt(3, transferAmount);
			transfer.execute();
		} catch (SQLException e) {
			System.out.println("transfer failed, please try again later");
		}

	}

	private int getFirstUserAccount(int intInput) {
		ResultSet rs = getMyAccounts(intInput);
		int[] allAccounts = new int[5];
		int i = 0;
		try {
			while (rs.next() && i < allAccounts.length) {
				allAccounts[i] = rs.getInt("account_id");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allAccounts[0];

	}

	private boolean isItMyAccount(int account, User user) {
		boolean isIt;

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccounts = conn.prepareStatement("select * from accounts where owner_id=?");
			getAccounts.setInt(1, user.getUserId());
			ResultSet rs = getAccounts.executeQuery();
			while (rs.next()) {
				isIt = account == rs.getInt("account_id");
				if (isIt) {
					return isIt;
				} else {
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
					.prepareStatement("insert into accounts (owner_id, approved, current_balance) values (?,null,?");
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

	public ResultSet getMyAccounts(int userId) {
		ResultSet rs = null;

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccount = conn.prepareStatement("Select (account_id, current_balance) from accounts where owner_id=? and approved=true");
			getAccount.setInt(1, userId);
			rs = getAccount.executeQuery();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return rs;
	}

}
