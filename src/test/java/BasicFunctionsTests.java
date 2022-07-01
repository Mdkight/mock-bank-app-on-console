import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.revature.objects.User;
import com.revature.utils.UserDataPostgres;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BasicFunctionsTests {
	UserDataPostgres userPost;
	User user;
	
	@Test
	@BeforeEach
	public void createUser() {
		user = new User();
		userPost = new UserDataPostgres();
	}

	@Test
	public void checkPasswordTest() {
		//TODO create password test
	}
	
	
}
