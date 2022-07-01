package com.revature.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDataPostgres {
	
	Connection conn;
	
	public void display(ResultSet res) {
		try {
			while(res.next()) {
				System.out.println(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void withdraw() {
		// TODO Auto-generated method stub
		
	}

	public void deposit() {
		// TODO Auto-generated method stub
		
	}

	public void selfTransfer() {
		// TODO Auto-generated method stub
		
	}

	public void otherTransfer() {
		// TODO Auto-generated method stub
		
	}

	public void newAccount() {
		// TODO Auto-generated method stub
		
	}
	
	

}
