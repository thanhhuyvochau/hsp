package fu.hbs.service.impl;

import fu.hbs.entities.User;
import fu.hbs.repositoties.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class UserServiceImplTestGetUserbyEmail {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserbyEmailWhenUserExistsThenReturnUser() {
        // Arrange
        String email = "test@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.getUserByEmail(email)).thenReturn(user);

        // Act
        User result = userService.getUserbyEmail(email);

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void testGetUserbyEmailWhenUserDoesNotExistThenReturnNull() {
        // Arrange
        String email = "test@gmail.com";
        when(userRepository.getUserByEmail(email)).thenReturn(null);

        // Act
        User result = userService.getUserbyEmail(email);

        // Assert
        assertNull(result);
    }
}