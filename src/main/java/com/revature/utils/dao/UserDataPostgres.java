package com.revature.utils.dao;

import java.sql.*;

import com.revature.objects.User;
import com.revature.utils.ConnectionUtils;

public class UserDataPostgres  {
	
	Connection conn;


	public User getUser(String username) {
		User currentUser= new User();
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getUser = conn.prepareStatement("Select * from users where username=?");
			getUser.setString(1, username);
			ResultSet rs= getUser.executeQuery();
			while(rs.next()) {
			currentUser.setUserId(rs.getInt(1));
			currentUser.setUserRole(rs.getString(2));
			currentUser.setUsername(rs.getString(3));
			currentUser.setPassword(rs.getString(4));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return currentUser;
	}


	public void createCustomer(String username, String password) {
		
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement createUser = conn.prepareStatement("Insert into users (user_role, username, password) values (CUSTOMER,?,?)");
			createUser.setString(1, username);
			createUser.setString(2, password);
			createUser.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

	}


	public void deleteUser(int userId) {
		
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement deleteUser = conn.prepareStatement("Delete * from users where user_id=?");
			deleteUser.setInt(1, userId);
			deleteUser.execute();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}


	public ResultSet getAllUsernames() {
		ResultSet rs =null;
		try {
			conn = ConnectionUtils.getInstance().getConnection();
			PreparedStatement getAllUsernames = conn.prepareStatement("Select username from users");
			rs = getAllUsernames.executeQuery();
		} catch (SQLException e) {
			// 
			e.printStackTrace();
		}
		
		return rs;
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
