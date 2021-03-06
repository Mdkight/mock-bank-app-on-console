package com.revature.main;

import java.sql.SQLException;
import java.util.Scanner;

import com.revature.menus.CustomerMenu;
import com.revature.menus.EmployeeMenu;
import com.revature.menus.Menu;
import com.revature.objects.User;

/*## Requirements
1. Functionality should reflect the below user stories.
2. Data is stored in a database.
3. Data Access is performed through the use of JDBC in a data layer consisting of Data Access Objects.
4. All input is received using the java.util.Scanner class.
5. A minimum of one (1) JUnit test is written to test some functionality.


## User Stories

* X As a user, I can login.
* X As a user, I can register for a customer account.
* 
* X As the system, I reject invalid transactions.
    * Ex:
        * A withdrawal that would result in a negative balance.
        * A deposit or withdrawal of negative money.
* 
*X As a customer, I can apply for a new bank account with a starting balance.
*X As a customer, I can view the balance of a specific account.
*X As a customer, I can make a withdrawal or deposit to a specific account.
*X As a customer, I can post a money transfer to another account.
*X As a customer, I can accept a money transfer from another account.
*  
*X As an employee, I can approve or reject an account.
*X As an employee, I can view a customer's bank accounts.
*X As an employee, I can view a log of all transactions.
*/

public class MainFunction {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int latch = 0;
		User currentUser = null;
		Menu mainMenu;

		System.out.println("Welcome to Java Bank");

		System.out.println("do you have an account with us?");
		System.out.println("Y or N");

		try {
		currentUser = BasicFunctions.hasAccount(scan.nextLine().toUpperCase());
			
		mainMenu = null;
		if (currentUser.getUserRole().toUpperCase().equals("EMPLOYEE")) {
			mainMenu = new EmployeeMenu();
		} else if (currentUser.getUserRole().toUpperCase().equals("CUSTOMER")) {
			mainMenu = new CustomerMenu();
		} else {
			System.out.println("something is wrong");
		}

		while (latch == 0) {
			Menu.displayMenu(mainMenu);
			int selection = BasicFunctions.getIntInput();
			mainMenu.makeSelection(selection, currentUser);
			
		}	
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

}
