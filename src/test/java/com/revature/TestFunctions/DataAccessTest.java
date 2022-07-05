package com.revature.TestFunctions;
import java.sql.SQLException;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.revature.main.BasicFunctions;
import com.revature.objects.Account;
import com.revature.objects.User;
import com.revature.utils.dao.AccountDataPostgres;
import com.revature.utils.dao.UserDataPostgres;

public class DataAccessTest {
	AccountDataPostgres acctPost = new AccountDataPostgres();
	Account testUser = new Account();

	@Test
	public void getFirstAccounttest() {
		testUser = acctPost.getFirstUserAccount(2);
		Assertions.assertEquals(testUser.getAccountId(), 1);	
	}
	
}
