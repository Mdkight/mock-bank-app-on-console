package com.revature.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.main.BasicFunctions;
import com.revature.objects.Account;
import com.revature.objects.Transfer;
import com.revature.objects.User;
import com.revature.utils.ConnectionUtils;

public class TransactionDataPostgres {

	Connection conn;

	public void getTransactions() {
		ResultSet transactions = null;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAllTransactions = conn.prepareStatement("select * from transactions");
			transactions = getAllTransactions.executeQuery();
			display(transactions);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void getTransactions(int userId) {
		ResultSet transactions = null;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getTransactions = conn.prepareStatement("select * from transactions where user_id=?");
			getTransactions.setInt(1, userId);
			transactions = getTransactions.executeQuery();
			display(transactions);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	
	}

	public void display(ResultSet res) {
		try {
			while (res.next()) {
				System.out.println();
				System.out.print("Transaction number: " + res.getInt(1));
				System.out.print("Account number: " + res.getInt(2));
				System.out.print("Transaction type: " + res.getString(4));
				System.out.print("Amount: " + res.getInt(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void seePendingTransfers(User currentUser) {
		ResultSet rs = null;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement seePending = conn.prepareStatement("Select transfers.transfer_id, transfers.amount  from accounts inner join transfers on transfers.destination_account_id = accounts.account_id where accounts.owner_id=?");
			seePending.setInt(1,currentUser.getUserId());
			rs = seePending.executeQuery();
			displayTransfer(rs);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		

	}

	private void displayTransfer(ResultSet res) {
		try {
			while (res.next()) {
				System.out.println();
				System.out.print("Transfer number: " + res.getInt(1));
				System.out.print("Amount: " + res.getInt(2));
				System.out.println();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void logTransaction(String transType, int amount, int accountId, int userId) {
		try {

			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement logTransaction = conn.prepareStatement(
					"insert into transactions (account_id, user_id, transaction_type, amount) values (?,?,?,?)");
//		"insert into transactions (account_id, user_id, transaction_type, amount) values (account_id=?, user_id=?, transaction_type=?, amount=?)");
			logTransaction.setInt(1, accountId);
			logTransaction.setInt(2, userId);			
			logTransaction.setString(3, transType);
			logTransaction.setInt(4, amount);
			logTransaction.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void selectPending() {
		System.out.println("please select the transfer you wish to accept");
		System.out.println("press B to go back");
		int transfer = BasicFunctions.getIntInput();
		if (transfer == 0) {

		} else {

			approveTransfer(getTransfer(transfer));
		}

	}

	private Transfer getTransfer(int transfer) {
		ResultSet rs = null;
		Transfer currentTransfer = new Transfer();

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getTransfer = conn.prepareStatement("select * from transfers where transfer_id=?");
			getTransfer.setInt(1, transfer);
			rs = getTransfer.executeQuery();
			currentTransfer.setTransferId(rs.getInt(1));
			currentTransfer.setTransactionNumber(rs.getInt(2));
			currentTransfer.setOriginId(rs.getInt(3));
			currentTransfer.setDestinationId(rs.getInt(4));
			currentTransfer.setTransferType(rs.getString(5));
			currentTransfer.setAmount(rs.getInt(6));
			return currentTransfer;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	private void approveTransfer(Transfer transfer) {
		AccountDataPostgres acctPost = new AccountDataPostgres();
		Account destinationAccount= acctPost.getAccount(transfer.getDestinationId());

		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement transferIn = conn.prepareStatement("update accounts set current_balance=? where acount_id=?");
			PreparedStatement removePending =conn.prepareStatement("delete * from transfers where transfer_id=?");
			transferIn.setInt(1, destinationAccount.getCurrentBalance()+transfer.getAmount());
			transferIn.setInt(2, destinationAccount.getAccountId());
			removePending.setInt(1, transfer.getTransferId());
			transferIn.execute();
			removePending.execute();
	
		} catch (SQLException e) {
			System.out.println("transfer failed, please try again later");
		}
		
	}
	
	

}
