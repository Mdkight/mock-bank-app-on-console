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

	public ResultSet getTransactions() {
		ResultSet transactions = null;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAllTransactions = conn.prepareStatement("select * from transactions");
			transactions = getAllTransactions.executeQuery();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return transactions;
	}

	public ResultSet getTransactions(int userId) {
		ResultSet transactions = null;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getTransactions = conn.prepareStatement("select * from transactions where user_id=?");
			getTransactions.setInt(1, userId);
			transactions = getTransactions.executeQuery();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return transactions;
	}

	public void display(ResultSet res) {
		try {
			while (res.next()) {
				System.out.println(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet seePendingTransactions(User currentUser) {
		ResultSet rs = null;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement seePending = conn.prepareStatement("select * from transfers where user_id=?");
			seePending.setInt(1, currentUser.getUserId());
			rs = seePending.executeQuery();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return rs;

	}

	public void logTransaction(String transType, int amount, int accountId, int ownerId) {
		try {

			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement logTransaction = conn.prepareStatement(
					"insert into transactions (transaction_type, amount, account_id, user_id) values transaction_type=?, amount=?, account_id=?, owner_id=?");
			logTransaction.setString(1, transType);
			logTransaction.setInt(2, amount);
			logTransaction.setInt(3, accountId);
			logTransaction.setInt(4, ownerId);
			logTransaction.execute();
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	private void approveTransfer(Transfer transfer) {
		AccountDataPostgres acctPost = new AccountDataPostgres();
		Account fromAccount= acctPost.getAccount(transfer.getOriginId());
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
