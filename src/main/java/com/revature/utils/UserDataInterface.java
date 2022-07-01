package com.revature.utils;

import java.sql.ResultSet;

import com.revature.objects.User;

public interface UserDataInterface {
	
	 User getUser(String username);
	
	 void deleteUser(int userId);

	 void createUser(String username, String password);
	 
	 ResultSet getAllUsernames();
	
	 ResultSet getTransactions();
	 
	 ResultSet getAccounts(int userId);

	ResultSet getAccounts(boolean status);
	

}
