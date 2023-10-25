package fu.hbs.service.impl;

import fu.hbs.dto.UserDto;
import fu.hbs.entities.User;
import fu.hbs.entities.UserRole;
import fu.hbs.repositoties.UserRepository;
import fu.hbs.repositoties.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTestSave {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;
    private UserRole userRole;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto("test", "test@gmail.com", "password");
        user = new User();
        user.setEmail(userDto.getUserEmail());
        user.setName(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getUserPassword()));
        user.setStatus(true);
        userRole = new UserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(5L);
    }




    @Test
    public void testSaveWhenAllDependenciesAreMockedAndUserDtoIsValidThenReturnUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User savedUser = userService.save(userDto);

        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getName(), savedUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRoleRepository, times(1)).save(any(UserRole.class));
    }

    @Test
    public void testSaveWhenUserDtoIsNullThenThrowException() {
        assertThrows(NullPointerException.class, () -> userService.save(null));
    }




    @Test
    public void testSaveWhenUserDtoIsInvalidThenThrowException() {
        UserDto invalidUserDto = new UserDto("", "", "");
        assertThrows(NullPointerException.class, () -> userService.save(invalidUserDto));
    }

    @Test
    public void testSaveWhenEmailIsInvalidThenThrowException() {
        UserDto invalidUserDto = new UserDto("", "effsdfsgdfgfdgddfgsgdfgfdgdfbdfgdfgdfgdfgdfgddgdsgdsgfsdfdsrfsfsfssg@gamil.com", "");
        assertThrows(NullPointerException.class, () -> userService.save(invalidUserDto));
    }


}