package fu.hbs.service.impl;

import fu.hbs.entities.User;
import fu.hbs.repositoties.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTestCheckUserbyEmail {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@gmail.com");
    }

    @Test
    public void testCheckUserbyEmailWhenUserExistsThenReturnTrue() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        boolean result = userService.checkUserbyEmail(user.getEmail());

        assertTrue(result);
    }

    @Test
    public void testCheckUserbyEmailWhenUserDoesNotExistThenReturnFalse() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        boolean result = userService.checkUserbyEmail(user.getEmail());

        assertFalse(result);
    }
}