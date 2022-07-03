package com.revature.utils.dao;

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
	static int latch=0;
	
	public void display(ResultSet res) {
		try {
			while(res.next()) {
				System.out.println(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void withdraw(Account act) {		
		int newBalance=checkWithdrawBalance(act);
		try {
			conn= ConnectionUtils.getInstance().getConnection();
			PreparedStatement withdraw = conn.prepareStatement("update accounts set current_balance=? where account_id=?");
			withdraw.setInt(1, newBalance);
			withdraw.setInt(2, act.getAccountId());
			act.setCurrentBalance(newBalance);
			System.out.println("your new balance for Account: "+ act.getAccountId()+ " is "+ act.getCurrentBalance());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private int checkWithdrawBalance(Account act) {
		latch=0;
		while(latch==0)
		System.out.println("how much would you like to withdraw?");
		int withdraw = BasicFunctions.getIntInput();
		int difference =act.getCurrentBalance()-withdraw;
		if(difference <0) {
			System.out.println("I'm sorry, you do not have that much in the account");
		}else {
			latch=1;
		}
		return difference;
	}
	public Account getAccount(int actId) {
		Account currentAccount =new Account();
		
		try {
			conn=ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAccount=conn.prepareStatement("Select * from accounts where account_id=?");
			getAccount.setInt(1, actId);
			ResultSet rs= getAccount.executeQuery();
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
				conn=ConnectionUtils.getInstance().getConnection();
				Statement getAccounts=conn.createStatement();
				ResultSet rs =getAccounts.executeQuery("select * from accounts where approved is null");
				display(rs);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		
	}

	public void deposit(Account act) {
		System.out.println("how much would you like to deposit?");
		int deposit = BasicFunctions.getIntInput();
		int sum =act.getCurrentBalance()+deposit;
		int newBalance=sum;
		try {
			conn= ConnectionUtils.getInstance().getConnection();
			PreparedStatement withdraw = conn.prepareStatement("update accounts set current_balance=? where account_id=?");
			withdraw.setInt(1, newBalance);
			withdraw.setInt(2, act.getAccountId());
			act.setCurrentBalance(newBalance);
			System.out.println("your new balance for Account: "+ act.getAccountId()+ " is "+ act.getCurrentBalance());
		} catch (SQLException e) {
			// 
			e.printStackTrace();
		}
		
	}


	public void selfTransfer() {
		// TODO Auto-generated method stub
		
	}

	public void otherTransfer() {
		// TODO Auto-generated method stub
		
	}

	public void newAccount(User currentUser) {
		System.out.println("What would you like to deposit as the starting balance of your new account?");
		int initialDeposit=BasicFunctions.getIntInput();
		try {
			conn=ConnectionUtils.getInstance().getConnection();
			PreparedStatement newAccount = conn.prepareStatement("insert into accounts (owner_id, approved, current_balance) values (?,null,?");
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
		if (acctNum==0) {
			
		}else {
			try {
				conn=ConnectionUtils.getInstance().getConnection();
				PreparedStatement approveAccount = conn.prepareStatement("update accounts set approved=true where account_id=?");
				approveAccount.setInt(1, acctNum);
				approveAccount.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	

}
