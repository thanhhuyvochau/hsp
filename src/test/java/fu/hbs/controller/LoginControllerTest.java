package fu.hbs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import fu.hbs.controller.LoginController;
import fu.hbs.service.dao.UserService;

public class LoginControllerTest {

	@Test
public void testLoginForm() {
		//Đầu tiên tạo ra một đối tượng LoginController mới.
        LoginController controller = new LoginController();
		Authentication auth = mock(Authentication.class);
		when(auth.getPrincipal()).thenReturn(new User("testuser", "password", 
				true, true, true, true, 
				java.util.Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER"))));
		String result = controller.loginForm(auth);
		assertEquals("redirect:/homepage", result);
	}

//	@Test
//	void testIndex() {
//		LoginController controller = new LoginController();
//		 UserService userService = mock(UserService.class);
//		Authentication auth = mock(Authentication.class);
//		UserDetails user = new User("testuser", "password", 
//				java.util.Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER")));
//		when(auth.getPrincipal()).thenReturn(user);
//
//		Model model = mock(Model.class);
//		
//		String result = controller.index(auth, model, null);
//		assertEquals("homepage", result);
//
//
//
//	}
	





	
	@Test
	public	void testIndexAdmin() {
		LoginController controller = new LoginController();
		Authentication auth = mock(Authentication.class);
		UserDetails user = new User("testuser", "password",
				java.util.Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
		when(auth.getPrincipal()).thenReturn(user);
		Model model = mock(Model.class);
		String result = controller.index(auth, model, null);
		assertEquals("homepage", result);
	}
	
	
	@Test
	public	void testIndexManagement() {
		LoginController controller = new LoginController();
		Authentication auth = mock(Authentication.class);
		UserDetails user = new User("testuser", "password",
				java.util.Collections.singletonList(new SimpleGrantedAuthority("MANAGEMENT")));
		when(auth.getPrincipal()).thenReturn(user);
		Model model = mock(Model.class);
		String result = controller.index(auth, model, null);
		assertEquals("homepage", result);
	}
	@Test
	public	void testIndexReceptionists() {
		LoginController controller = new LoginController();
		Authentication auth = mock(Authentication.class);
		UserDetails user = new User("testuser", "password",
				java.util.Collections.singletonList(new SimpleGrantedAuthority("Receptionists")));
		when(auth.getPrincipal()).thenReturn(user);
		Model model = mock(Model.class);
		String result = controller.index(auth, model, null);
		assertEquals("homepage", result);
	}

	@Test
	public void testIndexHousekeeping() {
		LoginController controller = new LoginController();
		Authentication auth = mock(Authentication.class);
		UserDetails user = new User("testuser", "password",
				java.util.Collections.singletonList(new SimpleGrantedAuthority("HOUSEKEEPING")));
		when(auth.getPrincipal()).thenReturn(user);
		Model model = mock(Model.class);
		String result = controller.index(auth, model, null);
		assertEquals("homepage", result);
	}

	@Test
	void testIndeAccounting() {
		LoginController controller = new LoginController();
		Authentication auth = mock(Authentication.class);
		UserDetails user = new User("testuser", "password",
				java.util.Collections.singletonList(new SimpleGrantedAuthority("ACCOUNTING")));
		when(auth.getPrincipal()).thenReturn(user);
		Model model = mock(Model.class);
		String result = controller.index(auth, model, null);
		assertEquals("homepage", result);
	}

	
	@Test
	public	void testIndexCustomer() {
		fu.hbs.entities.User user1 = new fu.hbs.entities.User();
		LoginController controller = new LoginController();
		Authentication auth = mock(Authentication.class);
		UserDetails user = new User("testuser", "password",
				java.util.Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER")));
		when(auth.getPrincipal()).thenReturn(user);
		Model model = mock(Model.class);
		HttpSession session = mock(HttpSession.class);
		UserService userService = mock(UserService.class);

		user1.setEmail(user.getUsername());
		user1.setName("John Doe");
		when(userService.getUserbyEmail(user.getUsername())).thenReturn(user1);
		String result = controller.index(auth, model, session);
		assertEquals("homepage", result);
		verify(session).setAttribute("accountDetail", user);
		verify(model).addAttribute("name", user1.getName());
	}

}