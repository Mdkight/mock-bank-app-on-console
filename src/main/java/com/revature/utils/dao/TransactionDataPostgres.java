package com.revature.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.utils.ConnectionUtils;

public class TransactionDataPostgres {
	
	Connection conn;
	
	public ResultSet getTransactions() {
		ResultSet transactions =null;
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
		ResultSet transactions =null;
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
			while(res.next()) {
				System.out.println(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
