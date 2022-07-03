package com.revature.TestFunctions;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.revature.main.BasicFunctions;
import com.revature.objects.User;
import com.revature.utils.dao.UserDataPostgres;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BasicFunctionsTests {
	UserDataPostgres userPost;
	User user;
	static Scanner scan = new Scanner(System.in);
	
	@Test
	@BeforeEach
	public void createUser() {
		user = new User();
		userPost = new UserDataPostgres();
	}

	@Test
	public void getIntInputTest() {
		System.out.println("enter a number");
		int result=BasicFunctions.getIntInput();
		System.out.println("enter the same number");
		int compare=scan.nextInt();
		Assertions.assertEquals(result,compare);
	}
	
	@Test
	public void testLogin() {
		user = BasicFunctions.userLogin();
		Assertions.assertEquals(user.getUsername(), "username");
	}
	
	
}
