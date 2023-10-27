package fu.hbs.service.impl;

import fu.hbs.entities.User;
import fu.hbs.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTestcheckPassword {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
    }
 
    @Test
    @DisplayName("Test the 'checkPasswordUser' method when the user's password is correct")
    public void testCheckPasswordUserWhenPasswordIsCorrectThenReturnTrue() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        boolean result = userService.checkPasswordUser(user.getEmail(), user.getPassword());


        assertTrue(result);

    }

    @Test
    @DisplayName("Test the 'checkPasswordUser' method when the user's password is incorrect")
    public void testCheckPasswordUserWhenPasswordIsIncorrectThenReturnFalse() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        boolean result = userService.checkPasswordUser(user.getEmail(), "wrongPassword");

        assertFalse(result);

    }

    @Test
    @DisplayName("Test the 'checkPasswordUser' method when the user does not exist")
    public void testCheckPasswordUserWhenUserDoesNotExistThenReturnFalse() {
        User user1;
        user1 = new User();
        user1.setEmail("test@gmail.com");
        user1.setPassword("password12");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user1);

        boolean result = userService.checkPasswordUser(user.getEmail(), user.getPassword());

        assertFalse(result);
    }
}