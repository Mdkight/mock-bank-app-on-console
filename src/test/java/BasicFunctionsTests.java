import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.revature.main.BasicFunctions;
import com.revature.objects.User;
import com.revature.utils.UserDataPostgres;

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
		int result=BasicFunctions.getIntInput();
		System.out.println("enter the same number");
		int compare=scan.nextInt();
		Assertions.assertEquals(result,compare);
	}
	
	
}
